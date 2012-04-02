package com.sbros.lit;


import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Lit implements ApplicationListener {
	
	public World world;
	public Body circle;
	public Body wall;
	public OrthographicCamera camera;
	int iters = 4;
	ShapeRenderer renderer;
	
	private Texture tex;
	private SpriteBatch batch;

	// 1 meter (Box2D) = 16 pixels
	public static final float RATIO = 16.0f;
	
	@Override
	public void create() {
		
		renderer = new ShapeRenderer();
		batch = new SpriteBatch();
		
		world = new World( new Vector2( 0.0f, -20.0f ), true );

		
		CircleShape shapeDef = new CircleShape();
		shapeDef.setRadius( 10f );
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shapeDef;
		fd.density = 1.0f;
		fd.friction = 0.3f;
		fd.restitution = 0.6f;
		
		BodyDef bd = new BodyDef();
		bd.allowSleep = false;
		bd.position.set( new Vector2( Gdx.graphics.getWidth()/ 2, Gdx.graphics.getHeight() / 2 )); 
		bd.angularDamping = 1f;
		
		circle = world.createBody(bd);
		circle.createFixture(fd);
		circle.setType( BodyDef.BodyType.DynamicBody );
		
		
		PolygonShape sd = new PolygonShape();
		sd.setAsBox(50f, 10f, new Vector2(50f,10f), 0);
		
		FixtureDef wfd = new FixtureDef();
		wfd.shape = sd;
		wfd.density = 1.0f;
		wfd.restitution = 0.1f;
		wfd.friction = 1.0f;
		
		BodyDef wbd = new BodyDef();
		wbd.allowSleep = true;
		wbd.position.set( new Vector2( Gdx.graphics.getWidth() / 2 + 50f, 10.0f )); 
		
		wall = world.createBody(wbd);
		wall.createFixture(wfd);
		wall.setType( BodyDef.BodyType.StaticBody );
		
		tex = new Texture(Gdx.files.internal("assets/indie.png"));
		
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}
	
	@Override
	public void render() {
		
		camera.update();
		camera.apply(Gdx.gl10);
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0f, 0, 1.0f);
		
		//Test spriteBatch
		batch.begin();
		batch.draw(tex, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 100, 100);
		batch.end();
		
		float msecs = Gdx.graphics.getDeltaTime() * 3000;
		
		float dt = (msecs / 1000.0f) / iters;
		wall.setTransform(Gdx.input.getX(), 0, 0);
		if(circle.getPosition().y < 10)
		{
			circle.setTransform(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		}
		for (int i = 0; i < iters; i++) {
			world.step(dt, 10, 10);
		}
		Random rand = new Random();
		float radius = 10;
		
		renderer.begin(ShapeType.FilledCircle);
		renderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		renderer.identity();
		renderer.translate(circle.getPosition().x, circle.getPosition().y, 0);
		renderer.filledCircle(0, 0, radius);
		renderer.end();
		
		renderer.begin(ShapeType.FilledRectangle);
		renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		renderer.identity();
		renderer.translate( Gdx.input.getX(), wall.getPosition().y , 0);
		renderer.rotate(0,0,1,0);
		renderer.filledRect( 0, 0, 100, 20);
		renderer.end();
		
		System.out.println("Ball : " + circle.getPosition().x + ":" + circle.getPosition().y);
		System.out.println("Wall : " + wall.getPosition().x + ":" + wall.getPosition().y);
	}

	@Override
	public void resize(int width, int height) {
        float aspectRatio = (float) width / (float) height;
        camera = new OrthographicCamera(3f * aspectRatio, 3f);

    }

	@Override
	public void resume() {
	}

}
