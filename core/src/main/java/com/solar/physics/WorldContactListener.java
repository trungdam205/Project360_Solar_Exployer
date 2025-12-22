package com.solar.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.solar.entities.Player;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (isFootContact(a, b)) {
            ((Player) a.getUserData()).setOnGround(true);
        }
        if (isFootContact(b, a)) {
            ((Player) b.getUserData()).setOnGround(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (isFootContact(a, b)) {
            ((Player) a.getUserData()).setOnGround(false);
        }
        if (isFootContact(b, a)) {
            ((Player) b.getUserData()).setOnGround(false);
        }
    }

    private boolean isFootContact(Fixture foot, Fixture other) {
        return "foot".equals(foot.getUserData()) && !other.isSensor();
    }

    @Override public void preSolve(Contact c, Manifold m) {}
    @Override public void postSolve(Contact c, ContactImpulse ci) {}
}
