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

package com.horvath;

import junit.framework.TestCase;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.optimization.GoalType;

/**
 * @author <a href="http://www.benmccann.com">Ben McCann</a>
 */
public class SimplexSolverTest extends TestCase {

  public void testSimplexSolver() throws UnboundedSolutionException, NoFeasibleSolutionException {

    LinearModel model = new LinearModel(new LinearObjectiveFunction(
        new double[] { 15, 10 }, 7, GoalType.MAXIMIZE));
    model.addConstraint(new LinearEquation(new double[] { 1, 0 }, Relationship.LEQ, 2));
    model.addConstraint(new LinearEquation(new double[] { 0, 1 }, Relationship.LEQ, 3));
    model.addConstraint(new LinearEquation(new double[] { 1, 1 }, Relationship.EQ, 4));

    SimplexSolver solver = new SimplexSolver(model);
    LinearEquation solution = solver.solve();
    assertEquals(2.0, solution.getCoefficients().getEntry(0));
    assertEquals(2.0, solution.getCoefficients().getEntry(1));
    assertEquals(57.0, solution.getRightHandSide());
  }

  /**
   * With no artificial variables needed (no equals and no greater than
   * constraints) we can go straight to Phase 2.
   */
  public void testModelWithNoArtificialVars() throws UnboundedSolutionException,
      NoFeasibleSolutionException {
    LinearModel model = new LinearModel(new LinearObjectiveFunction(
        new double[] { 15, 10 }, 0, GoalType.MAXIMIZE));
    model.addConstraint(new LinearEquation(new double[] { 1, 0 }, Relationship.LEQ, 2));
    model.addConstraint(new LinearEquation(new double[] { 0, 1 }, Relationship.LEQ, 3));
    model.addConstraint(new LinearEquation(new double[] { 1, 1 }, Relationship.LEQ, 4));

    SimplexSolver solver = new SimplexSolver(model);
    LinearEquation solution = solver.solve();
    assertEquals(2.0, solution.getCoefficients().getEntry(0));
    assertEquals(2.0, solution.getCoefficients().getEntry(1));
    assertEquals(50.0, solution.getRightHandSide());
  }

  public void testMinimization() throws UnboundedSolutionException, NoFeasibleSolutionException {
    LinearModel model = new LinearModel(new LinearObjectiveFunction(
        new double[] { -2, 1 }, -5, GoalType.MINIMIZE));
    model.addConstraint(new LinearEquation(new double[] { 1, 2 }, Relationship.LEQ, 6));
    model.addConstraint(new LinearEquation(new double[] { 3, 2 }, Relationship.LEQ, 12));
    model.addConstraint(new LinearEquation(new double[] { 0, 1 }, Relationship.GEQ, 0));

    SimplexSolver solver = new SimplexSolver(model);
    LinearEquation solution = solver.solve();
    assertEquals(4.0, solution.getCoefficients().getEntry(0));
    assertEquals(0.0, solution.getCoefficients().getEntry(1));
    assertEquals(-13.0, solution.getRightHandSide());
  }

  public void testSolutionWithNegativeDecisionVariable() throws UnboundedSolutionException,
      NoFeasibleSolutionException {
    LinearModel model = new LinearModel(new LinearObjectiveFunction(
        new double[] { -2, 1 }, 0, GoalType.MAXIMIZE));
    model.addConstraint(new LinearEquation(new double[] { 1, 1 }, Relationship.GEQ, 6));
    model.addConstraint(new LinearEquation(new double[] { 1, 2 }, Relationship.LEQ, 14));

    SimplexSolver solver = new SimplexSolver(model);
    LinearEquation solution = solver.solve();
    assertEquals(-2.0, solution.getCoefficients().getEntry(0));
    assertEquals(8.0, solution.getCoefficients().getEntry(1));
    assertEquals(12.0, solution.getRightHandSide());
  }

  public void testInfeasibleSolution() throws UnboundedSolutionException {
    LinearModel model = new LinearModel(new LinearObjectiveFunction(
        new double[] { 15 }, 0, GoalType.MAXIMIZE));
    model.addConstraint(new LinearEquation(new double[] { 1 }, Relationship.LEQ, 1));
    model.addConstraint(new LinearEquation(new double[] { 1 }, Relationship.GEQ, 3));

    SimplexSolver solver = new SimplexSolver(model);
    try {
      solver.solve();
      fail("An exception should have been thrown.");
    } catch (NoFeasibleSolutionException e) {
    }
  }

  public void testUnboundedSolution() throws NoFeasibleSolutionException {
    LinearModel model = new LinearModel(new LinearObjectiveFunction(
        new double[] { 15, 10 }, 0, GoalType.MAXIMIZE));
    model.addConstraint(new LinearEquation(new double[] { 1, 0 }, Relationship.EQ, 2));

    SimplexSolver solver = new SimplexSolver(model);
    try {
      solver.solve();
      fail("An exception should have been thrown.");
    } catch (UnboundedSolutionException e) {
    }
  }
  
  public void testRestrictVariablesToNonNegative() throws UnboundedSolutionException, NoFeasibleSolutionException {
    LinearModel model = new LinearModel(new LinearObjectiveFunction(
        new double[] { 409, 523, 70, 204, 339 }, 0, GoalType.MAXIMIZE));
    model.addConstraint(new LinearEquation(new double[] {    43,   56, 345,  56,    5 }, Relationship.LEQ,  4567456));
    model.addConstraint(new LinearEquation(new double[] {    12,   45,   7,  56,   23 }, Relationship.LEQ,    56454));
    model.addConstraint(new LinearEquation(new double[] {     8,  768,   0,  34, 7456 }, Relationship.LEQ,  1923421));
    model.addConstraint(new LinearEquation(new double[] { 12342, 2342,  34, 678, 2342 }, Relationship.GEQ,     4356));
    model.addConstraint(new LinearEquation(new double[] {    45,  678,  76,  52,   23 }, Relationship.EQ,    456356));
    
    SimplexSolver solver = new SimplexSolver(model, true);
    LinearEquation solution = solver.solve();
    assertEquals(2902.92783505155, solution.getCoefficients().getEntry(0), .0000001);
    assertEquals(480.419243986254, solution.getCoefficients().getEntry(1), .0000001);
    assertEquals(0.0, solution.getCoefficients().getEntry(2), .0000001);
    assertEquals(0.0, solution.getCoefficients().getEntry(3), .0000001);
    assertEquals(0.0, solution.getCoefficients().getEntry(4), .0000001);
    assertEquals(1438556.7491409, solution.getRightHandSide(), .0000001);
  }

  public void testSomething() throws UnboundedSolutionException, NoFeasibleSolutionException {
    LinearModel model = new LinearModel(new LinearObjectiveFunction(
        new double[] { 1, 1 }, 0, GoalType.MAXIMIZE));
    model.addConstraint(new LinearEquation(new double[] { 1, 1 }, Relationship.EQ,  0));
    
    SimplexSolver solver = new SimplexSolver(model, true);
    LinearEquation solution = solver.solve();
    assertEquals(0, solution.getRightHandSide(), .0000001);
  }
  
  public void testLargeModel() throws UnboundedSolutionException, NoFeasibleSolutionException {
    double[] objective = new double[] {
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 12, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        12, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 12, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 12, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 12, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 12, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1};
    
    LinearModel model = new LinearModel(new LinearObjectiveFunction(objective, 0, GoalType.MINIMIZE));
    model.addConstraint(equationFromString(objective.length, "x0 + x1 + x2 + x3 - x12 = 0"));
    model.addConstraint(equationFromString(objective.length, "x4 + x5 + x6 + x7 + x8 + x9 + x10 + x11 - x13 = 0"));
    model.addConstraint(equationFromString(objective.length, "x4 + x5 + x6 + x7 + x8 + x9 + x10 + x11 >= 49"));
    model.addConstraint(equationFromString(objective.length, "x0 + x1 + x2 + x3 >= 42"));
    model.addConstraint(equationFromString(objective.length, "x14 + x15 + x16 + x17 - x26 = 0"));
    model.addConstraint(equationFromString(objective.length, "x18 + x19 + x20 + x21 + x22 + x23 + x24 + x25 - x27 = 0"));
    model.addConstraint(equationFromString(objective.length, "x14 + x15 + x16 + x17 - x12 = 0"));
    model.addConstraint(equationFromString(objective.length, "x18 + x19 + x20 + x21 + x22 + x23 + x24 + x25 - x13 = 0"));
    model.addConstraint(equationFromString(objective.length, "x28 + x29 + x30 + x31 - x40 = 0"));
    model.addConstraint(equationFromString(objective.length, "x32 + x33 + x34 + x35 + x36 + x37 + x38 + x39 - x41 = 0"));
    model.addConstraint(equationFromString(objective.length, "x32 + x33 + x34 + x35 + x36 + x37 + x38 + x39 >= 49"));
    model.addConstraint(equationFromString(objective.length, "x28 + x29 + x30 + x31 >= 42"));
    model.addConstraint(equationFromString(objective.length, "x42 + x43 + x44 + x45 - x54 = 0"));
    model.addConstraint(equationFromString(objective.length, "x46 + x47 + x48 + x49 + x50 + x51 + x52 + x53 - x55 = 0"));
    model.addConstraint(equationFromString(objective.length, "x42 + x43 + x44 + x45 - x40 = 0"));
    model.addConstraint(equationFromString(objective.length, "x46 + x47 + x48 + x49 + x50 + x51 + x52 + x53 - x41 = 0"));
    model.addConstraint(equationFromString(objective.length, "x56 + x57 + x58 + x59 - x68 = 0"));
    model.addConstraint(equationFromString(objective.length, "x60 + x61 + x62 + x63 + x64 + x65 + x66 + x67 - x69 = 0"));
    model.addConstraint(equationFromString(objective.length, "x60 + x61 + x62 + x63 + x64 + x65 + x66 + x67 >= 51"));
    model.addConstraint(equationFromString(objective.length, "x56 + x57 + x58 + x59 >= 44"));
    model.addConstraint(equationFromString(objective.length, "x70 + x71 + x72 + x73 - x82 = 0"));
    model.addConstraint(equationFromString(objective.length, "x74 + x75 + x76 + x77 + x78 + x79 + x80 + x81 - x83 = 0"));
    model.addConstraint(equationFromString(objective.length, "x70 + x71 + x72 + x73 - x68 = 0"));
    model.addConstraint(equationFromString(objective.length, "x74 + x75 + x76 + x77 + x78 + x79 + x80 + x81 - x69 = 0"));
    model.addConstraint(equationFromString(objective.length, "x84 + x85 + x86 + x87 - x96 = 0"));
    model.addConstraint(equationFromString(objective.length, "x88 + x89 + x90 + x91 + x92 + x93 + x94 + x95 - x97 = 0"));
    model.addConstraint(equationFromString(objective.length, "x88 + x89 + x90 + x91 + x92 + x93 + x94 + x95 >= 51"));
    model.addConstraint(equationFromString(objective.length, "x84 + x85 + x86 + x87 >= 44"));
    model.addConstraint(equationFromString(objective.length, "x98 + x99 + x100 + x101 - x110 = 0"));
    model.addConstraint(equationFromString(objective.length, "x102 + x103 + x104 + x105 + x106 + x107 + x108 + x109 - x111 = 0"));
    model.addConstraint(equationFromString(objective.length, "x98 + x99 + x100 + x101 - x96 = 0"));
    model.addConstraint(equationFromString(objective.length, "x102 + x103 + x104 + x105 + x106 + x107 + x108 + x109 - x97 = 0"));
    model.addConstraint(equationFromString(objective.length, "x112 + x113 + x114 + x115 - x124 = 0"));
    model.addConstraint(equationFromString(objective.length, "x116 + x117 + x118 + x119 + x120 + x121 + x122 + x123 - x125 = 0"));
    model.addConstraint(equationFromString(objective.length, "x116 + x117 + x118 + x119 + x120 + x121 + x122 + x123 >= 49"));
    model.addConstraint(equationFromString(objective.length, "x112 + x113 + x114 + x115 >= 42"));
    model.addConstraint(equationFromString(objective.length, "x126 + x127 + x128 + x129 - x138 = 0"));
    model.addConstraint(equationFromString(objective.length, "x130 + x131 + x132 + x133 + x134 + x135 + x136 + x137 - x139 = 0"));
    model.addConstraint(equationFromString(objective.length, "x126 + x127 + x128 + x129 - x124 = 0"));
    model.addConstraint(equationFromString(objective.length, "x130 + x131 + x132 + x133 + x134 + x135 + x136 + x137 - x125 = 0"));
    model.addConstraint(equationFromString(objective.length, "x140 + x141 + x142 + x143 - x152 = 0"));
    model.addConstraint(equationFromString(objective.length, "x144 + x145 + x146 + x147 + x148 + x149 + x150 + x151 - x153 = 0"));
    model.addConstraint(equationFromString(objective.length, "x144 + x145 + x146 + x147 + x148 + x149 + x150 + x151 >= 59"));
    model.addConstraint(equationFromString(objective.length, "x140 + x141 + x142 + x143 >= 42"));
    model.addConstraint(equationFromString(objective.length, "x154 + x155 + x156 + x157 - x166 = 0"));
    model.addConstraint(equationFromString(objective.length, "x158 + x159 + x160 + x161 + x162 + x163 + x164 + x165 - x167 = 0"));
    model.addConstraint(equationFromString(objective.length, "x154 + x155 + x156 + x157 - x152 = 0"));
    model.addConstraint(equationFromString(objective.length, "x158 + x159 + x160 + x161 + x162 + x163 + x164 + x165 - x153 = 0"));
    model.addConstraint(equationFromString(objective.length, "x83 + x82 - x168 = 0"));
    model.addConstraint(equationFromString(objective.length, "x111 + x110 - x169 = 0"));
    model.addConstraint(equationFromString(objective.length, "x170 - x182 = 0"));
    model.addConstraint(equationFromString(objective.length, "x171 - x183 = 0"));
    model.addConstraint(equationFromString(objective.length, "x172 - x184 = 0"));
    model.addConstraint(equationFromString(objective.length, "x173 - x185 = 0"));
    model.addConstraint(equationFromString(objective.length, "x174 - x186 = 0"));
    model.addConstraint(equationFromString(objective.length, "x175 + x176 - x187 = 0"));
    model.addConstraint(equationFromString(objective.length, "x177 - x188 = 0"));
    model.addConstraint(equationFromString(objective.length, "x178 - x189 = 0"));
    model.addConstraint(equationFromString(objective.length, "x179 - x190 = 0"));
    model.addConstraint(equationFromString(objective.length, "x180 - x191 = 0"));
    model.addConstraint(equationFromString(objective.length, "x181 - x192 = 0"));
    model.addConstraint(equationFromString(objective.length, "x170 - x26 = 0"));
    model.addConstraint(equationFromString(objective.length, "x171 - x27 = 0"));
    model.addConstraint(equationFromString(objective.length, "x172 - x54 = 0"));
    model.addConstraint(equationFromString(objective.length, "x173 - x55 = 0"));
    model.addConstraint(equationFromString(objective.length, "x174 - x168 = 0"));
    model.addConstraint(equationFromString(objective.length, "x177 - x169 = 0"));
    model.addConstraint(equationFromString(objective.length, "x178 - x138 = 0"));
    model.addConstraint(equationFromString(objective.length, "x179 - x139 = 0"));
    model.addConstraint(equationFromString(objective.length, "x180 - x166 = 0"));
    model.addConstraint(equationFromString(objective.length, "x181 - x167 = 0"));
    model.addConstraint(equationFromString(objective.length, "x193 - x205 = 0"));
    model.addConstraint(equationFromString(objective.length, "x194 - x206 = 0"));
    model.addConstraint(equationFromString(objective.length, "x195 - x207 = 0"));
    model.addConstraint(equationFromString(objective.length, "x196 - x208 = 0"));
    model.addConstraint(equationFromString(objective.length, "x197 - x209 = 0"));
    model.addConstraint(equationFromString(objective.length, "x198 + x199 - x210 = 0"));
    model.addConstraint(equationFromString(objective.length, "x200 - x211 = 0"));
    model.addConstraint(equationFromString(objective.length, "x201 - x212 = 0"));
    model.addConstraint(equationFromString(objective.length, "x202 - x213 = 0"));
    model.addConstraint(equationFromString(objective.length, "x203 - x214 = 0"));
    model.addConstraint(equationFromString(objective.length, "x204 - x215 = 0"));
    model.addConstraint(equationFromString(objective.length, "x193 - x182 = 0"));
    model.addConstraint(equationFromString(objective.length, "x194 - x183 = 0"));
    model.addConstraint(equationFromString(objective.length, "x195 - x184 = 0"));
    model.addConstraint(equationFromString(objective.length, "x196 - x185 = 0"));
    model.addConstraint(equationFromString(objective.length, "x197 - x186 = 0"));
    model.addConstraint(equationFromString(objective.length, "x198 + x199 - x187 = 0"));
    model.addConstraint(equationFromString(objective.length, "x200 - x188 = 0"));
    model.addConstraint(equationFromString(objective.length, "x201 - x189 = 0"));
    model.addConstraint(equationFromString(objective.length, "x202 - x190 = 0"));
    model.addConstraint(equationFromString(objective.length, "x203 - x191 = 0"));
    model.addConstraint(equationFromString(objective.length, "x204 - x192 = 0"));
    
    SimplexSolver solver = new SimplexSolver(model, true);
    LinearEquation solution = solver.solve();
    assertEquals(7518.0, solution.getRightHandSide(), .0000001);
  }
  
  /**
   * Converts a test string to a {@link LinearEquation}.
   * Ex: x0 + x1 + x2 + x3 - x12 = 0
   */
  private LinearEquation equationFromString(int numCoefficients, String s) {
    Relationship relationship;
    if (s.contains(">=")) {
      relationship = Relationship.GEQ;
    } else if (s.contains("<=")) {
      relationship = Relationship.LEQ;
    } else if (s.contains("=")) {
      relationship = Relationship.EQ;
    } else {
      throw new IllegalArgumentException();
    }
    
    String[] equationParts = s.split("[>|<]?=");
    double rhs = Double.parseDouble(equationParts[1].trim());
    
    RealVector lhs = new Array2DRowRealMatrix(numCoefficients);
    String left = equationParts[0].replaceAll(" ?x", "");
    String[] coefficients = left.split(" ");
    for (String coefficient : coefficients) {
      double value = coefficient.charAt(0) == '-' ? -1 : 1;
      int index = Integer.parseInt(coefficient.replaceFirst("[+|-]", "").trim());
      lhs.setEntry(index, value);
    }
    return new LinearEquation(lhs, relationship, rhs);
  }
  
}
