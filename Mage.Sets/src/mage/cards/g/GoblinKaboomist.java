/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.g;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Outcome;
import mage.constants.TargetController;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.game.permanent.token.LandMineToken;
import mage.players.Player;

/**
 *
 * @author Quercitron
 */
public class GoblinKaboomist extends CardImpl {

    public GoblinKaboomist(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{R}");
        this.subtype.add(SubType.GOBLIN);
        this.subtype.add(SubType.WARRIOR);

        this.power = new MageInt(1);
        this.toughness = new MageInt(2);

        // At the beginning of your upkeep, create a colorless artifact token named Land Mine
        // with "{R}, Sacrifice this artifact: This artifact deals 2 damage to target attacking creature without flying."
        // Then flip a coin.  If you lose the flip, Goblin Kaboomist deals 2 damage to itself.
        Ability ability = new BeginningOfUpkeepTriggeredAbility(new CreateTokenEffect(new LandMineToken()), TargetController.YOU, false);
        ability.addEffect(new GoblinKaboomistFlipCoinEffect());
        this.addAbility(ability);
    }

    public GoblinKaboomist(final GoblinKaboomist card) {
        super(card);
    }

    @Override
    public GoblinKaboomist copy() {
        return new GoblinKaboomist(this);
    }
}

class GoblinKaboomistFlipCoinEffect extends OneShotEffect {

    public GoblinKaboomistFlipCoinEffect() {
        super(Outcome.Damage);
    }

    public GoblinKaboomistFlipCoinEffect(final GoblinKaboomistFlipCoinEffect effect) {
        super(effect);
    }

    @Override
    public GoblinKaboomistFlipCoinEffect copy() {
        return new GoblinKaboomistFlipCoinEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getControllerId());
        Permanent permanent = game.getPermanent(source.getSourceId());
        if (player != null && permanent != null) {
            if (!player.flipCoin(game)) {
                String message = new StringBuilder(permanent.getLogName()).append(" deals 2 damage to itself").toString();
                game.informPlayers(message);
                permanent.damage(2, source.getSourceId(), game, false, true);
            }
            return true;
        }
        return false;
    }

}
