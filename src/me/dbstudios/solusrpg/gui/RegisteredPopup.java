package me.dbstudios.solusrpg.gui;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.gui.RpgPopup;
import me.dbstudios.solusrpg.util.siml.Document;

public class RegisteredPopup {
	private final Class<? extends RpgPopup> popupClass;
	private final Document document;

	public RegisteredPopup(Class<? extends RpgPopup> popupClass, Document document) {
		this.popupClass = popupClass;
		this.document = document;
	}

	public RpgPopup createPopup() {
		try {
			Constructor<? extends RpgPopup> ctor = popupClass.getConstructor(Document.class);

			return ctor.newInstance(this.document);
		} catch (Exception e) {
			SolusRpg.log(Level.WARNING, String.format("Could not create popup from class %s: %s", popupClass.getSimpleName(), e.getClass().getSimpleName()));

			if (Configuration.is("logging.verbose"))
				e.printStackTrace();
		}

		return null;
	}
}