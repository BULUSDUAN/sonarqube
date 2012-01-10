/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.java.api;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JavaMethodTest {
  @Test
  public void shouldCreateReference() {
    String key = "org.foo.Bar#hello(LString;)V";
    JavaMethod method = JavaMethod.createRef(key);
    assertThat(method.getKey(), is(key));
    assertThat(method.getClassName(), is("org.foo.Bar"));
    assertThat(method.getLongName(), is(key));
    assertThat(method.getName(), is("hello(LString;)V"));
    assertThat(method.getSignature(), is("hello(LString;)V"));
  }

  @Test
  public void shouldCreateReferenceFromClassAndSignature() {
    String className = "org.foo.Bar";
    String signature = "hello(LString;)V";
    JavaMethod method = JavaMethod.createRef(JavaClass.create(className), signature);
    assertThat(method.getKey(), is(className + "#" + signature));
    assertThat(method.getClassName(), is(className));
    assertThat(method.getName(), is(signature));
    assertThat(method.getSignature(), is(signature));
  }
}
