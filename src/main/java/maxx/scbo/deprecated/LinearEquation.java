// Copyright 2009 Google Inc.

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package maxx.scbo.deprecated;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * A constraint for a {@link LinearModel}.
 * 
 * @author <a href="http://www.benmccann.com">Ben McCann</a>
 */
public class LinearEquation {

  private final ArrayRealVector leftHandSide;
  private final ExRelationship exRelationship;
  private final double rightHandSide;

  public LinearEquation(double[] coefficients, ExRelationship exRelationship, double rightHandSide) {
    this(new ArrayRealVector(coefficients), exRelationship, rightHandSide);
  }

  /**
   * TODO.
   * 
   * @param leftHandSide TODO
   * @param exRelationship TODO
   * @param rightHandSide TODO
   */
  public LinearEquation(ArrayRealVector leftHandSide, ExRelationship exRelationship,
      double rightHandSide) {
    this.leftHandSide = leftHandSide;
    this.exRelationship = exRelationship;
    this.rightHandSide = rightHandSide;
  }

  /**
   * TODO.
   * 
   * @param leftHandSide TODO
   * @param exRelationship TODO
   * @param rightHandSide TODO
   */
  public LinearEquation(ArrayRealVector leftHandSide, ExRelationship exRelationship,
      RealVector rightHandSide) {
    this.leftHandSide = leftHandSide.subtract(rightHandSide);
    this.exRelationship = exRelationship;
    this.rightHandSide = 0;
  }

  public ArrayRealVector getCoefficients() {
    return leftHandSide;
  }

  public ExRelationship getRelationship() {
    return exRelationship;
  }

  public double getRightHandSide() {
    return rightHandSide;
  }

}
