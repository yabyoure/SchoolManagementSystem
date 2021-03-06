package com.app.sms.ui.gestionnaires.controllers.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import com.app.sms.exceptions.NotFoundDataException;
import com.app.sms.models.Enseignant;
import com.app.sms.ui.gestionnaires.controllers.IUIConsulterEnseignantController;
import com.app.sms.ui.gestionnaires.impl.UIConsulterEnseignant;

public class UIConsulterEnseignantController implements IUIConsulterEnseignantController {
	private UIConsulterEnseignant uIConsulterEnseignant;
	private Enseignant enseignant;
	/**
	 * @param uINouveauEnseignant
	 */
	public UIConsulterEnseignantController(UIConsulterEnseignant uIConsulterEnseignant, Enseignant enseignant) {
		this.uIConsulterEnseignant = uIConsulterEnseignant;
		this.enseignant = enseignant;
		
		addUpdateListener();
		addDeleteListener();
		try {

			List<Enseignant> enseignants = Enseignant.list();
			this.uIConsulterEnseignant.loadData(enseignants);
			this.uIConsulterEnseignant.displayNotification("Done successfully !");
		} catch (SQLException exception) {
			this.uIConsulterEnseignant.displayErrorMessage(exception.getMessage());
		}
	}
	
	@Override
	public void addUpdateListener() {
		uIConsulterEnseignant.addUpdateListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setEnseignant (uIConsulterEnseignant);
					enseignant.update ();
					uIConsulterEnseignant.loadData(Enseignant.list());
					uIConsulterEnseignant.resetFormUI();
					uIConsulterEnseignant.displayNotification("Done successfully !");
				} catch (SQLException | NotFoundDataException exception) {
					uIConsulterEnseignant.displayErrorMessage(exception.getMessage());
				}
			}
		});
	}
	
	@Override
	public void addDeleteListener() {
		uIConsulterEnseignant.addDeleteListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setEnseignant (uIConsulterEnseignant);
					enseignant.delete();
					uIConsulterEnseignant.loadData(Enseignant.list());
					uIConsulterEnseignant.resetFormUI();
					uIConsulterEnseignant.displayNotification("Done successfully !");
				} catch (NotFoundDataException | SQLException exception) {
					uIConsulterEnseignant.displayErrorMessage(exception.getMessage());
				}
				
			}
		});
	}
	
	@Override
	public void addGoListener() {
		uIConsulterEnseignant.addGoListener (new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {			
				try {
					uIConsulterEnseignant.loadData(Enseignant.list());
					uIConsulterEnseignant.resetFormUI();
					uIConsulterEnseignant.displayNotification("Done successfully !");
				} catch (SQLException exception) {
					uIConsulterEnseignant.displayErrorMessage(exception.getMessage());
				}
			}
		});
	}

	public void run() {
		uIConsulterEnseignant.showMe();
	}

	public void setEnseignant (UIConsulterEnseignant uIConsulterEnseignant) {
		enseignant.setId(Integer.parseInt(uIConsulterEnseignant.getId()));
		enseignant.setNom(uIConsulterEnseignant.getNom());
		enseignant.setPrenom(uIConsulterEnseignant.getPrenom());
		enseignant.setGenre(uIConsulterEnseignant.getGenre());
		enseignant.setTelephone(uIConsulterEnseignant.getTelephone());
		enseignant.setEmail(uIConsulterEnseignant.getEmail());
		enseignant.setLogin(uIConsulterEnseignant.getLogin());
		enseignant.setPassword(uIConsulterEnseignant.getPassword());
		enseignant.setPicturePath(uIConsulterEnseignant.getPicturePath());
		enseignant.setIdClasses(uIConsulterEnseignant.getSelectedIdClasses());
	}
}
