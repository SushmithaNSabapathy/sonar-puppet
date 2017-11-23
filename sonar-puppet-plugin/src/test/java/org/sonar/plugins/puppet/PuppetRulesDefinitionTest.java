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
package org.sonar.plugins.puppet;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Rule;
import org.sonar.puppet.checks.CheckList;
import org.sonar.puppet.checks.TodoTagPresenceCheck;

import static org.fest.assertions.Assertions.assertThat;

public class PuppetRulesDefinitionTest {

  @Test
  public void test() {
    PuppetRulesDefinition rulesDefinition = new PuppetRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository("puppet");

    assertThat(repository.name()).isEqualTo("SonarQube");
    assertThat(repository.language()).isEqualTo("puppet");
    assertThat(repository.rules()).hasSize(65);

    RulesDefinition.Rule todoRule = repository.rule(TodoTagPresenceCheck.class.getAnnotation(Rule.class).key());
    assertThat(todoRule).isNotNull();
    assertThat(todoRule.name()).isEqualTo(TodoTagPresenceCheck.class.getAnnotation(Rule.class).name());
  }

}
