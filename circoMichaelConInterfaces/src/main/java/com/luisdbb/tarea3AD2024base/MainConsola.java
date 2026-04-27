package com.luisdbb.tarea3AD2024base;

//DE LA VERSION DE CONSOLA
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.luisdbb.tarea3AD2024base.services.*;

public class MainConsola {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(Tarea3Ad2024baseApplication.class, args);

		Menus menus = new Menus(ctx.getBean(ServicioEspectaculos.class), ctx.getBean(ServicioPersonas.class),
				ctx.getBean(ServicioArtistas.class), ctx.getBean(ServicioLogin.class));

		menus.iniciar();
	}
}
