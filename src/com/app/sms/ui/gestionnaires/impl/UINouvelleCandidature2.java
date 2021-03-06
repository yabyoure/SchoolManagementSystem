package com.app.sms.ui.gestionnaires.impl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.app.sms.exceptions.BadFormatDataException;
import com.app.sms.exceptions.DateOutOfBoundsException;
import com.app.sms.exceptions.NotAvailableDateFormatException;
import com.app.sms.models.Enseignant;
import com.app.sms.models.enums.Genre;
import com.app.sms.ui.gestionnaires.IUINouvelleCandidature;
import com.app.sms.ui.gestionnaires.listeners.FilledFieldChecker;
import com.app.sms.ui.gestionnaires.listeners.SelectDiplomeListener;
import com.app.sms.ui.impl.AbstractUIOperation;
import com.app.sms.utils.DateFormatValidator;
import com.app.sms.utils.Utilitaire;
import java.awt.SystemColor;

public class UINouvelleCandidature2 extends AbstractUIOperation implements IUINouvelleCandidature {	
	/**
	 * 
	 * Les éléments du contrôleur ...
	 */
	private JButton submitButton;
	private JTextField id;
	private JTextField nom;
	private JTextField prenom;
	private JTextField email;
	private JTextField telephone;
	private JRadioButton homme;
	private JRadioButton femme;
	private JButton selectPictureButton;
	private JLabel pictureLabel;
	private String picturePath=null;
	private JTextField birthday;
	private HashMap<Integer,String> diplomesUrl = new HashMap<>();
	private HashMap<Integer,JLabel> diplomesFile = new HashMap<>();
	private HashMap<Integer,JButton> diplomesButton = new HashMap<>();
	private JComboBox<String> responsables;
	ArrayList <JPanel> diplomes;
	private String cvUrl;
	private JLabel pathCVLabel;
	private JCheckBox analysable;
	
	public UINouvelleCandidature2() {
		centerPanel.setBackground(new Color(204, 204, 102));
		setForeground(new Color(0, 0, 0));
		setBackground(new Color(192, 192, 192));
		setPreferredSize(new Dimension(750, 485));
		operationIcon.setIcon(new ImageIcon(UINouvelleCandidature2.class.getResource("/images/images (1).jpg")));
		operationLabel.setText("Candidatures");
		
		JPanel formPanel = new JPanel();
		formPanel.setBackground(SystemColor.inactiveCaption);
		centerPanel.add(formPanel, BorderLayout.CENTER);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(SystemColor.inactiveCaptionBorder);
		mainPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Nouvelle candidature", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		submitButton = new JButton("Submit");
		submitButton.setPreferredSize(new Dimension(67, 20));
		submitButton.setMinimumSize(new Dimension(67, 20));
		submitButton.setMaximumSize(new Dimension(67, 20));
		submitButton.setEnabled(false);
		
		JButton clearButton = new JButton("Clear");
		clearButton.setPreferredSize(new Dimension(59, 20));
		clearButton.setMinimumSize(new Dimension(59, 20));
		clearButton.setMaximumSize(new Dimension(59, 20));
		clearButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetUI ();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(67, 20));
		cancelButton.setMinimumSize(new Dimension(67, 20));
		cancelButton.setMaximumSize(new Dimension(67, 20));
		cancelButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitWithConfirmation();
			}
		});
		GroupLayout formPanelLayout = new GroupLayout(formPanel);
		formPanelLayout.setHorizontalGroup(
			formPanelLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(formPanelLayout.createSequentialGroup()
					.addContainerGap(535, Short.MAX_VALUE)
					.addComponent(submitButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(clearButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(formPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
					.addGap(12))
		);
		formPanelLayout.setVerticalGroup(
			formPanelLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(formPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(formPanelLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(submitButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(clearButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(0))
		);
		
		JLabel idLabel = new JLabel("Id :");
		JLabel nomLabel = new JLabel("Nom :");
		JLabel prenomLabel = new JLabel("Prénom :");
		JLabel emailLabel = new JLabel("Email :");
		JLabel telephoneLabel = new JLabel("Téléphone :");
		JLabel birthdayLabel = new JLabel("Date de Naissance :");
		
		id = new JTextField();
		id.setText("-1");
		id.setEnabled(false);
		id.setEditable(false);
		id.setColumns(10);
		
		nom = new JTextField();
		nom.setColumns(10);
		
		prenom = new JTextField();
		prenom.setColumns(10);
		
		email = new JTextField();
		email.setColumns(10);
		
		telephone = new JTextField();
		telephone.setColumns(10);
		
		JPanel notificationPanel = new JPanel();
		notificationPanel.setBorder(new TitledBorder(null, "Notification", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel diplomesPanel = new JPanel();
		diplomesPanel.setToolTipText(null);
		diplomesPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Dipl\u00F4mes", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		pictureLabel = new JLabel("");
		pictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pictureLabel.setIcon(new ImageIcon(UINouvelleCandidature2.class.getResource("/images/inconnu.png")));
		
		JLabel sexeLabel = new JLabel("Sexe :");
		
		homme = new JRadioButton(Genre.Homme.name());
		homme.setSelected(true);
		femme = new JRadioButton(Genre.Femme.name());
		ButtonGroup genres = new ButtonGroup();
		genres.add(homme);
		genres.add(femme);
		
		selectPictureButton = new JButton("Select picture");
		selectPictureButton.setToolTipText("Sélectionner la photo du candidat ...");
		selectPictureButton.setMinimumSize(new Dimension(88, 20));
		selectPictureButton.setMaximumSize(new Dimension(88, 20));
		selectPictureButton.setPreferredSize(new Dimension(93, 20));
		selectPictureButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
		       selectAndSetPictureFromBrowser ();
			}
		});
		
		JPanel cvPanel = new JPanel();
		cvPanel.setBorder(new TitledBorder(null, "Curriculum Vitae", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		birthday = new JTextField();
		setDefaultBirthdayDateFormat();
		birthday.setColumns(20);
		birthday.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (birthday.getText().isEmpty()) {
					setDefaultBirthdayDateFormat ();
				}				
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				birthday.setForeground(Color.BLACK);
				birthday.setText(null);
			}
		});
		
		JPanel affectationPanel = new JPanel();
		FlowLayout fl_affectationPanel = (FlowLayout) affectationPanel.getLayout();
		fl_affectationPanel.setAlignment(FlowLayout.LEFT);
		affectationPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Affectation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanelLayout.setHorizontalGroup(
			mainPanelLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(mainPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(mainPanelLayout.createSequentialGroup()
							.addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(birthdayLabel)
								.addComponent(telephoneLabel)
								.addComponent(emailLabel)
								.addComponent(prenomLabel)
								.addComponent(nomLabel)
								.addComponent(idLabel)
								.addComponent(sexeLabel))
							.addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
							.addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(birthday, 191, 191, 191)
								.addGroup(mainPanelLayout.createSequentialGroup()
									.addComponent(homme)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(femme))
								.addComponent(id, 191, 191, 191)
								.addComponent(nom, 191, 191, 191)
								.addComponent(prenom, 191, 191, 191)
								.addComponent(email, 191, 191, 191)
								.addComponent(telephone, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)))
						.addComponent(cvPanel, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(mainPanelLayout.createSequentialGroup()
							.addComponent(notificationPanel, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING)
							.addGroup(mainPanelLayout.createSequentialGroup()
								.addComponent(diplomesPanel, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(selectPictureButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(pictureLabel))
								.addGap(15))
							.addGroup(mainPanelLayout.createSequentialGroup()
								.addComponent(affectationPanel, GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
								.addContainerGap()))))
		);
		mainPanelLayout.setVerticalGroup(
			mainPanelLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(mainPanelLayout.createSequentialGroup()
					.addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(mainPanelLayout.createSequentialGroup()
							.addGroup(mainPanelLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(mainPanelLayout.createSequentialGroup()
									.addContainerGap()
									.addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(id, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(idLabel))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(nom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(nomLabel))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(prenom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(prenomLabel))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(emailLabel))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(telephoneLabel)
										.addComponent(telephone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(mainPanelLayout.createSequentialGroup()
									.addComponent(pictureLabel, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(selectPictureButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(birthdayLabel)
								.addComponent(birthday, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(diplomesPanel, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(mainPanelLayout.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(mainPanelLayout.createSequentialGroup()
							.addGroup(mainPanelLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(sexeLabel)
								.addComponent(homme)
								.addComponent(femme))
							.addGap(9)
							.addComponent(cvPanel, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
						.addGroup(mainPanelLayout.createSequentialGroup()
							.addComponent(affectationPanel, 0, 0, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(notificationPanel, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		notificationPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		notificationPanel.add(scrollPane, BorderLayout.CENTER);
		
		notification.setEditable(false);
		notification.setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane.setViewportView(notification);
		
		notification.setEditable(false);
		scrollPane.setViewportView(notification);
		
		JLabel analyseParLabel = new JLabel("A analyser par :");
		affectationPanel.add(analyseParLabel);
		
		responsables = new JComboBox<>();
		responsables.setEnabled(false);
		responsables.addItem("Choisissez un enseignant ...");
		responsables.setToolTipText("Cliquez sur le bouton radio pour activer ...");
				
		responsables.setPreferredSize(new Dimension(220, 22));
		affectationPanel.add(responsables);
		
		analysable = new JCheckBox("");
		affectationPanel.add(analysable);
		analysable.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {					
					if (analysable.isSelected()) {
						responsables.setEnabled(true);
						for ( Enseignant enseignant : Enseignant.list() ) {
							responsables.addItem( enseignant.getId() + Utilitaire.SEPARATEUR + enseignant.getNom() + " " + enseignant.getPrenom());
						}
					} else {
						responsables.removeAllItems();
						responsables.addItem("Choisissez un enseignant ...");
						responsables.setEnabled(false);
					}
				} catch (SQLException exception) {
					displayErrorMessage("An Error Occured while loading liste enseignants : " + exception.getMessage());
				}
			}
		});
		cvPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel buttonSlectionCVPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonSlectionCVPanel.getLayout();
		flowLayout.setVgap(7);
		cvPanel.add(buttonSlectionCVPanel);
		
		JButton ajouterCVButton = new JButton("Ajouter un CV");
		ajouterCVButton.setMaximumSize(new Dimension(101, 20));
		ajouterCVButton.setMinimumSize(new Dimension(101, 20));
		ajouterCVButton.setPreferredSize(new Dimension(101, 20));
		ajouterCVButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ajouterCV ();
			}
		});
		buttonSlectionCVPanel.add(ajouterCVButton);
		
		JPanel pathPanel = new JPanel();
		FlowLayout pathPanelLayout = (FlowLayout) pathPanel.getLayout();
		pathPanelLayout.setVgap(8);
		cvPanel.add(pathPanel);
		
		pathCVLabel = new JLabel("");
		pathCVLabel.setForeground(Color.BLUE);
		pathCVLabel.setFont(new Font("Tahoma", Font.ITALIC, 10));
		pathPanel.add(pathCVLabel);
		diplomesPanel.setLayout(new GridLayout(5, 1, 0, 0));
		
		diplomes = new ArrayList<>();
		
		for ( int i = 0 ; i < 5 ; i ++ ) {
			JPanel diplome = new JPanel();
			FlowLayout diplomeFlowLayout = (FlowLayout) diplome.getLayout();
			diplomeFlowLayout.setVgap(2);
			diplomeFlowLayout.setAlignment(FlowLayout.LEFT);
			
			JButton addButton = new JButton("+");
			addButton.setToolTipText("Ajouter un diplome (.doc,.pdf,.jpg, ...)");
			addButton.setMinimumSize(new Dimension(43, 20));
			addButton.setMaximumSize(new Dimension(43, 20));
			addButton.setPreferredSize(new Dimension(43, 20));
			addButton.addActionListener(new SelectDiplomeListener (this, i));
			JLabel diplomeUrlLabel = new JLabel("");
			diplomeUrlLabel.setForeground(Color.BLUE);
			diplomeUrlLabel.setFont(new Font("Tahoma", Font.ITALIC, 10));
			diplomesFile.put(i, diplomeUrlLabel);
			diplomesButton.put(i, addButton);
			
			diplome.add(addButton);
			
			diplome.add(diplomeUrlLabel);
			diplomes.add (diplome);
			diplomesPanel.add(diplome);
		}
		
		mainPanel.setLayout(mainPanelLayout);
		formPanel.setLayout(formPanelLayout);
		/*
		 * Contrôle de la saisie du champ Libelle et Coéfficient avant activation du bouton de validation 
		 * 
		 * */	
		ArrayList<JTextField> textFields = new ArrayList<>();
		textFields.add(nom);
		textFields.add(prenom);
		textFields.add(email);
		textFields.add(telephone);
		FilledFieldChecker submitButtonChecker = new FilledFieldChecker(submitButton, textFields);
		nom.getDocument().addDocumentListener(submitButtonChecker);
		prenom.getDocument().addDocumentListener(submitButtonChecker);
		email.getDocument().addDocumentListener(submitButtonChecker);
		telephone.getDocument().addDocumentListener(submitButtonChecker);
	}

	private void setDefaultBirthdayDateFormat() {
		birthday.setText("JJ-MM-AAAA");
		birthday.setForeground(Color.LIGHT_GRAY);
	}

	private void ajouterCV() {
		JFileChooser chooser = new JFileChooser();
        int status = chooser.showOpenDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            cvUrl = file.getAbsolutePath();            
            if (file.getName().length() > Utilitaire.MAX_SIZE_FILENAME) {
            	String cvFileName = file.getName();
            	cvFileName = cvFileName.substring(0, Utilitaire.MAX_SIZE_FILENAME - 3) + "...";
            	pathCVLabel.setText(cvFileName);
            } else {
            	pathCVLabel.setText(file.getName());
            }
        }		
	}
	
	private void setPicture(ImageIcon imageIcon) {
		pictureLabel.setIcon(imageIcon);
	}
	
	private void setPicture(URL resource) {
		setPicture(new ImageIcon(resource));
	}
	
	private void selectAndSetPictureFromBrowser() {
		JFileChooser chooser = new JFileChooser();
        int status = chooser.showOpenDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            picturePath = file.getAbsolutePath();
            ImageIcon imageIcon = Utilitaire.getImageIcon (this, picturePath ); 
            setPicture(imageIcon);
        }
	}

	
	
	@Override
	public void resetUI() {
		this.id.setText("-1");
		this.nom.setText(null);
		this.prenom.setText(null);
		this.telephone.setText(null);
		this.email.setText(null);
		displayNotification(null);
		setPicture(UINouvelleCandidature2.class.getResource("/images/inconnu.png"));
		
		// diplomesUrl
		Set<Entry<Integer, String>> setUrl = diplomesUrl.entrySet();
		Iterator<Entry<Integer, String>> itUrl = setUrl.iterator();
		while (itUrl.hasNext()) {
			itUrl.next().setValue(null);
		}
		// diplomesFile
		Set<Entry<Integer, JLabel>> setFile = diplomesFile.entrySet();
		Iterator<Entry<Integer, JLabel>> itFile = setFile.iterator();
		while (itFile.hasNext()) {
			itFile.next().getValue().setText(null);
		}
		// diplomesButton
		Set<Entry<Integer, JButton>> setButton = diplomesButton.entrySet();
		Iterator<Entry<Integer, JButton>> itButton = setButton.iterator();
		while (itButton.hasNext()) {
			itButton.next().getValue().setText("+");
		}
		
		pathCVLabel.setText(null);
		setDefaultBirthdayDateFormat();
		
		responsables.removeAllItems();
		responsables.setEnabled(false);
		responsables.addItem("Choisissez un enseignant ...");
		analysable.setSelected(false);
	}
	
	@Override
	public void addSubmitListener(ActionListener listenForSubmitButton) {
		submitButton.addActionListener(listenForSubmitButton);
	}
	
	public void addAnalysableListener(ActionListener listenForAnalysableCheckBox) {
		analysable.addActionListener(listenForAnalysableCheckBox);
	}

	@Override
	public String getId() {
		return id.getText();
	}
	
	@Override
	public String getNom() {
		return nom.getText();
	}
	
	@Override
	public String getPrenom() {
		return prenom.getText();
	}
	
	@Override
	public String getEmail() {
		return email.getText();
	}

	@Override
	public String getTelephone() {
		return telephone.getText();
	}

	@Override
	public void addSelectPictureListener(ActionListener listenerForSelectPictureButton) {
		selectPictureButton.addActionListener(listenerForSelectPictureButton);
	}

	@Override
	public String getGenre () {
		if (homme.isSelected()) return Genre.Homme.name() ;
		else return Genre.Femme.name() ;
	}
	
	@Override
	public String getPicturePath() {
		return picturePath;
	}
	
	@Override
	public String getBirthday() throws BadFormatDataException, NumberFormatException, DateOutOfBoundsException, NotAvailableDateFormatException {
		if (DateFormatValidator.validate(birthday.getText(), DateFormatValidator.DateFormat.JJ_MM_AAAA)) {
			return birthday.getText();
		} else return null;
	}
	
	@Override
	public String getCVUrl() {
		return cvUrl;
	}
	
	@Override
	public List<String> getDiplomes() {
		List<String> urls = new ArrayList<>();
		Set<Entry<Integer, String>> setUrl = diplomesUrl.entrySet();
		Iterator<Entry<Integer, String>> itUrl = setUrl.iterator();
		while (itUrl.hasNext()) {
			urls.add(itUrl.next().getValue());
		}
		return urls;
	}
	
	@Override
	public int getSelectedEnseignantId () {
		if ( responsables.isEnabled() ) {
			String selectedEnseignant = responsables.getItemAt(responsables.getSelectedIndex());
			String [] splitInfos = selectedEnseignant.split(Utilitaire.SEPARATEUR);
			return Integer.parseInt(splitInfos[0]);
		}
		return -1;
	}
	
	public void setButtonAt (int index, String text) {
		diplomesButton.get(index).setText(text);
	}
	
	public void addDiplomesUrl(Integer key, String absolutePath) {
		diplomesUrl.put(key, absolutePath);
	}
	
	public void removeDiplomesUrlAt(int index) {
		diplomesUrl.remove(index);
	}
	
	public void setDiplomesFileAt(int index, String texte) {
		if ( texte.length() > Utilitaire.MAX_SIZE_FILENAME ) {
			texte = texte.substring(0, Utilitaire.MAX_SIZE_FILENAME - 3) + "...";
		}
		diplomesFile.get(index).setText(texte);
	}
}