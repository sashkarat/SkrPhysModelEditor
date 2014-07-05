package org.skr.physmodel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import org.skr.PhysModelEditor.PhysWorld;
import org.skr.physmodel.animatedactorgroup.AnimatedActorGroup;

/**
 * Created by rat on 11.06.14.
 */
public class BodyItem extends PhysItem {

    private static int g_id = -1;

    private int id = -1;

    public BodyItem( int id ) {
        if ( id < 0 ) {
            this.id = ++g_id;
        } else {
            this.id = id;
            if ( g_id < id )
                g_id = id;
        }
    }

    public int getId() {
        return id;
    }

    Body body = null;

    Array< FixtureSet > fixtureSets = new Array<FixtureSet>();

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Array<FixtureSet> getFixtureSets() {
        return fixtureSets;
    }

    public void setFixtureSets(Array<FixtureSet> fixtureSets) {
        this.fixtureSets = fixtureSets;
    }

    public FixtureSet addNewFixtureSet( String name ) {
        FixtureSet fs = new FixtureSet( this );
        fs.setName( name );
        fixtureSets.add( fs );
        return fs;
    }

    public void removeFixtureSet( FixtureSet fixtureSet ) {
        int indexOf = fixtureSets.indexOf( fixtureSet, true );
        if ( indexOf < 0 )
            return;
        fixtureSets.removeIndex( indexOf );
        fixtureSet.removeAllFixtures();
    }

    public void updateTransform() {
        if ( body == null )
            return;
        Vector2 pos = PhysWorld.get().physToView( body.getPosition() );
        float angle = body.getAngle();
        setPosition( pos.x, pos.y );
        setRotation(MathUtils.radiansToDegrees * angle );

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        updateTransform();
        super.draw(batch, parentAlpha);
    }

    static final Matrix3 mtx = new Matrix3();

    public static Vector2 stageToBodyItemLocal( BodyItem bodyItem, Vector2 coord ) {
        mtx.idt();

        mtx.translate( bodyItem.getX(), bodyItem.getY() );
        mtx.rotate( bodyItem.getRotation() );

        coord.mul( mtx.inv() );
        return coord;
    }

}
