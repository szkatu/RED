package org.robotframework.ide.eclipse.main.plugin.tableeditor.source.colouring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.robotframework.red.junit.Conditions.absent;
import static org.robotframework.red.junit.Conditions.present;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.rules.Token;
import org.junit.Test;
import org.rf.ide.core.testdata.text.read.IRobotLineElement;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;
import org.rf.ide.core.testdata.text.read.separators.Separator;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.source.colouring.ISyntaxColouringRule.PositionedTextToken;

public class VariableDefinitionRuleTest {

    private final VariableDefinitionRule testedRule = new VariableDefinitionRule(new Token("token"));

    @Test
    public void ruleIsApplicableOnlyForRobotTokens() {
        assertThat(testedRule.isApplicable(new RobotToken())).isTrue();
        assertThat(testedRule.isApplicable(new Separator())).isFalse();
        assertThat(testedRule.isApplicable(mock(IRobotLineElement.class))).isFalse();
    }

    @Test
    public void variableDefinitionIsRecognized() {
        boolean thereWasName = false;
        for (final RobotToken token : TokensSource.createTokens()) {
            final Optional<PositionedTextToken> evaluatedToken = evaluate(token);

            if (token.getText().contains("var_def")) {
                thereWasName = true;

                assertThat(evaluatedToken).is(present());
                assertThat(evaluatedToken.get().getPosition())
                        .isEqualTo(new Position(token.getStartOffset(), token.getText().length()));
                assertThat(evaluatedToken.get().getToken().getData()).isEqualTo("token");

            } else {
                assertThat(evaluatedToken).is(absent());
            }
        }
        assertThat(thereWasName).isTrue();
    }

    @Test
    public void variableDefinitionIsRecognized_evenWhenPositionIsInsideToken() {
        boolean thereWasName = false;
        for (final RobotToken token : TokensSource.createTokens()) {
            final int positionInsideToken = new Random().nextInt(token.getText().length());
            final Optional<PositionedTextToken> evaluatedToken = evaluate(token, positionInsideToken);

            if (token.getText().contains("var_def")) {
                thereWasName = true;

                assertThat(evaluatedToken).is(present());
                assertThat(evaluatedToken.get().getPosition())
                        .isEqualTo(new Position(token.getStartOffset(), token.getText().length()));
                assertThat(evaluatedToken.get().getToken().getData()).isEqualTo("token");

            } else {
                assertThat(evaluatedToken).is(absent());
            }
        }
        assertThat(thereWasName).isTrue();
    }

    private Optional<PositionedTextToken> evaluate(final RobotToken token) {
        return evaluate(token, 0);
    }

    private Optional<PositionedTextToken> evaluate(final RobotToken token, final int position) {
        return testedRule.evaluate(token, position, new ArrayList<IRobotLineElement>());
    }
}
