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
package mage.cards.r;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.Mode;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.dynamicvalue.common.PermanentsOnBattlefieldCount;
import mage.abilities.effects.ContinuousEffectImpl;
import mage.abilities.effects.common.continuous.SetPowerToughnessSourceEffect;
import mage.abilities.effects.common.counter.AddCountersTargetEffect;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Layer;
import mage.constants.Outcome;
import mage.constants.SubLayer;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.filter.FilterPermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.SupertypePredicate;
import mage.filter.predicate.permanent.CounterPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.TargetPermanent;

/**
 *
 * @author TheElk801
 */
public class RimefeatherOwl extends CardImpl {

    private static final FilterPermanent filter = new FilterPermanent("Permanents with ice counters on them");
    private static final FilterPermanent filter2 = new FilterPermanent("snow permanents on the battlefield");

    static {
        filter.add(new CounterPredicate(CounterType.ICE));
        filter2.add(new SupertypePredicate(SuperType.SNOW));
    }

    public RimefeatherOwl(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{5}{U}{U}");

        this.addSuperType(SuperType.SNOW);
        this.subtype.add(SubType.BIRD);
        this.power = new MageInt(0);
        this.toughness = new MageInt(0);

        // Flying
        this.addAbility(FlyingAbility.getInstance());

        // Rimefeather Owl's power and toughness are each equal to the number of snow permanents on the battlefield.
        this.addAbility(new SimpleStaticAbility(Zone.ALL, new SetPowerToughnessSourceEffect(new PermanentsOnBattlefieldCount(filter2), Duration.EndOfGame)));

        // {1}{snow}: Put an ice counter on target permanent.
        Ability ability = new SimpleActivatedAbility(
                Zone.BATTLEFIELD,
                new AddCountersTargetEffect(CounterType.ICE.createInstance())
                        .setText("Put an ice counter on target permanent."),
                new ManaCostsImpl("{1}{S}")
        );
        ability.addTarget(new TargetPermanent());
        this.addAbility(ability);

        // Permanents with ice counters on them are snow.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new RimefeatherOwlEffect(Duration.WhileOnBattlefield, filter)));
    }

    public RimefeatherOwl(final RimefeatherOwl card) {
        super(card);
    }

    @Override
    public RimefeatherOwl copy() {
        return new RimefeatherOwl(this);
    }
}

class RimefeatherOwlEffect extends ContinuousEffectImpl {

    private final FilterPermanent filter;

    public RimefeatherOwlEffect(Duration duration, FilterPermanent filter) {
        super(duration, Layer.TypeChangingEffects_4, SubLayer.NA, Outcome.Detriment);
        this.filter = filter;
    }

    public RimefeatherOwlEffect(final RimefeatherOwlEffect effect) {
        super(effect);
        this.filter = effect.filter;
    }

    @Override
    public RimefeatherOwlEffect copy() {
        return new RimefeatherOwlEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        for (Permanent permanent : game.getBattlefield().getActivePermanents(filter, source.getControllerId(), source.getSourceId(), game)) {
            permanent.addSuperType(SuperType.SNOW);

        }
        return true;
    }

    @Override
    public String getText(Mode mode) {
        return "Permanents with ice counters on them are snow.";
    }
}
