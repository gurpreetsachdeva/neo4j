/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.cypher.internal.frontend.v3_4.semantics

import org.neo4j.cypher.internal.aux.v3_4.DummyPosition
import org.neo4j.cypher.internal.aux.v3_4.symbols._
import org.neo4j.cypher.internal.v3_4.expressions.Divide

class DivideTest extends InfixExpressionTestBase(Divide(_, _)(DummyPosition(0))) {

  // Infix specializations:
  // 1 / 1 => 1
  // 1 / 1.1 => 0.909
  // 1.1 / 1 => 1.1
  // 1.1 / 1.1 => 1.0

  test("shouldHandleAllSpecializations") {
    testValidTypes(CTInteger, CTInteger)(CTInteger)
    testValidTypes(CTInteger, CTFloat)(CTFloat)
    testValidTypes(CTFloat, CTInteger)(CTFloat)
    testValidTypes(CTFloat, CTFloat)(CTFloat)
  }

  test("shouldHandleCombinedSpecializations") {
    testValidTypes(CTFloat | CTInteger, CTFloat | CTInteger)(CTFloat | CTInteger)
  }

  test("shouldFailTypeCheckWhenAddingIncompatible") {
    testInvalidApplication(CTInteger, CTBoolean)(
      "Type mismatch: expected Float or Integer but was Boolean"
    )
    testInvalidApplication(CTBoolean, CTInteger)(
      "Type mismatch: expected Float or Integer but was Boolean"
    )
  }
}