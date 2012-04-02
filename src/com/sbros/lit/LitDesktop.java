package com.sbros.lit;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class LitDesktop { 
	public static void main(String[] args) {
		new JoglApplication(new Lit(), "Lit", 800, 480, false);
	}
}
