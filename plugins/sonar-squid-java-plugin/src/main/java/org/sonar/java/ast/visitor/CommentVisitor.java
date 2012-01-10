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
package org.sonar.java.ast.visitor;

import java.util.Arrays;
import java.util.List;

import org.sonar.squid.api.SourceFile;
import org.sonar.squid.measures.Metric;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * SONAR-3093: Note that this visitor saves {@link Metric#COMMENT_LINES} into {@link org.sonar.squid.api.SourceCode}
 * as sum of {@link Metric#COMMENT_LINES} and {@link Metric#COMMENTED_OUT_CODE_LINES} from {@link org.sonar.squid.test.Source}.
 */
public class CommentVisitor extends JavaAstVisitor {

  private static final List<Integer> WANTED_TOKENS = Arrays.asList(TokenTypes.RCURLY);

  @Override
  public List<Integer> getWantedTokens() {
    return WANTED_TOKENS;
  }

  @Override
  public void visitToken(DetailAST ast) {
    int startAtLine = peekSourceCode().getStartAtLine();
    int endAtLine = peekSourceCode().getEndAtLine();
    int commentedOutCodeLines = getSource().getMeasure(Metric.COMMENTED_OUT_CODE_LINES, startAtLine, endAtLine);
    int commentLines = getSource().getMeasure(Metric.COMMENT_LINES, startAtLine, endAtLine);
    peekSourceCode().setMeasure(Metric.COMMENTED_OUT_CODE_LINES, commentedOutCodeLines);
    peekSourceCode().setMeasure(Metric.COMMENT_LINES, commentLines + commentedOutCodeLines);
    peekSourceCode().setMeasure(Metric.COMMENT_BLANK_LINES, getSource().getMeasure(Metric.COMMENT_BLANK_LINES, startAtLine, endAtLine));
  }

  @Override
  public void visitFile(DetailAST ast) {
    SourceFile file = (SourceFile) peekSourceCode();
    file.addNoSonarTagLines(getSource().getNoSonarTagLines());
    file.setMeasure(Metric.HEADER_COMMENT_LINES, getSource().getMeasure(Metric.HEADER_COMMENT_LINES));
    int commentedOutCodeLines = getSource().getMeasure(Metric.COMMENTED_OUT_CODE_LINES);
    int commentLines = getSource().getMeasure(Metric.COMMENT_LINES);
    file.setMeasure(Metric.COMMENTED_OUT_CODE_LINES, commentedOutCodeLines);
    file.setMeasure(Metric.COMMENT_LINES, commentLines + commentedOutCodeLines);
    file.setMeasure(Metric.COMMENT_BLANK_LINES, getSource().getMeasure(Metric.COMMENT_BLANK_LINES));
  }

}
