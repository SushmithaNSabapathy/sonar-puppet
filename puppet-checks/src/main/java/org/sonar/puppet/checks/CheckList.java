/*
 * SonarQube Puppet Analyzer
 * Copyright (C) 2017-2017 David RACODON
 * david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.puppet.checks;

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class CheckList {

  public static final String REPOSITORY_KEY = "puppet";
  public static final String REPOSITORY_NAME = "SonarQube";

  private CheckList() {
  }

  public static List<Class> getChecks() {
    return ImmutableList.<Class>of(
      ArrowsAlignmentCheck.class,
      AutoLoaderLayoutCheck.class,
      BOMCheck.class,
      BooleanInversionCheck.class,
      CaseWithoutDefaultCheck.class,
      ClassAndDefineNamingConventionCheck.class,
      CollapsibleIfStatementsCheck.class,
      CommentConventionCheck.class,
      CommentRegularExpressionCheck.class,
      ComplexExpressionCheck.class,
      DocumentClassesAndDefinesCheck.class,
      DeprecatedNodeInheritanceCheck.class,
      DeprecatedOperatorsCheck.class,
      DuplicateConditionCheck.class,
      DuplicatedHashKeysCheck.class,
      DuplicatedParametersCheck.class,
      EmptyBlocksCheck.class,
      EnsureOrderingCheck.class,
      ExcessSpacesWhenAccessingHashesArraysCheck.class,
      FaultyQuoteUsageCheck.class,
      FileEnsurePropertyIsValidCheck.class,
      FileModeCheck.class,
      FileNameCheck.class,
      FixmeTagPresenceCheck.class,
      IfStatementFormattingCheck.class,
      IfStatementWithoutElseClauseCheck.class,
      ImportStatementUsedCheck.class,
      IndentationCheck.class,
      InheritsAcrossNamespaceCheck.class,
      LineLengthCheck.class,
      LintIgnoreCheck.class,
      LiteralBooleanInComparisonCheck.class,
      MetadataJsonFilePresentCheck.class,
      MissingNewLineAtEndOfFileCheck.class,
      NestedClassesOrDefinesCheck.class,
      NestedCasesAndSelectorsCheck.class,
      NestedIfStatementsCheck.class,
      NoopUsageCheck.class,
      NosonarTagPresenceCheck.class,
      OneIncludePerLineCheck.class,
      ParsingErrorCheck.class,
      PuppetURLModulesCheck.class,
      QuotedBooleanCheck.class,
      QuotedEnumerableCheck.class,
      ReadmeFilePresentCheck.class,
      RequiredParametersFirstCheck.class,
      ResourceDefaultFirstCheck.class,
      ResourceDefaultNamingConventionCheck.class,
      ResourceWithSelectorCheck.class,
      RightToLeftChainingArrowsCheck.class,
      SelectorWithoutDefaultCheck.class,
      SingleQuotedStringContainingVariablesCheck.class,
      TabCharacterCheck.class,
      TestsDirectoryPresentCheck.class,
      TodoTagPresenceCheck.class,
      TooComplexClassesAndDefinesCheck.class,
      TrailingCommasCheck.class,
      TrailingWhitespaceCheck.class,
      UnquotedNodeNameCheck.class,
      UnquotedResourceTitleCheck.class,
      UselessIfStatementParenthesesCheck.class,
      UserResourceLiteralNameCheck.class,
      UserResourcePasswordNotSetCheck.class,
      VariableNamingConventionCheck.class,
      VariableNotEnclosedInBracesCheck.class
    );
  }
}
