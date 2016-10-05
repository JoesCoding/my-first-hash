/**
 * Driver program for programming assignment 4.
 * 
 * San Diego State University CS 310 Fall 2015
 */

//import data_structures.BalancedTree;
//import data_structures.BinarySearchTree;
import data_structures.DictionaryADT;
import data_structures.HashTable;



import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Exoplanets extends JFrame {

  private static final long serialVersionUID = 1L;

  private class Exit extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public Exit() {
      putValue(NAME, "Exit");
      putValue(SHORT_DESCRIPTION, "Terminate the application.");
    }

    public void actionPerformed(ActionEvent event) {
      System.exit(0);
    }
  }

  private class Load extends AbstractAction {
    public Load() {
      putValue(NAME, "Load");
      putValue(SHORT_DESCRIPTION, "Load an exoplanet .csv file.");
    }

    public void actionPerformed(ActionEvent event) {
      systemList.clearSelection();
      knownPlanetSystems = buildDictionary(selectCsvPlanetFile());
      Vector<String> keySet = getPlanetSystemKeys();
      systemList.setListData(keySet);
    }
  }

  private class SystemSelected implements ListSelectionListener {

    JList<String> attachedList;
    JTextArea attachedArea;

    public SystemSelected(JList<String> list, JTextArea area) {
      attachedList = list;
      attachedArea = area;
    }

    public void valueChanged(ListSelectionEvent arg0) {
      if (!arg0.getValueIsAdjusting()) {

        String selected = attachedList.getSelectedValue();
        if (selected == null) {
          return;
        }
        attachedArea.setText(knownPlanetSystems.getValue(selected).toString());
        attachedArea.moveCaretPosition(0);
      }
    }
  }

  DictionaryADT<String, StarSystem> knownPlanetSystems;

  private final Action actionExit = new Exit();
  private final Action actionLoad = new Load();

  private JList<String> systemList;

  private File selectCsvPlanetFile() {
    JFileChooser fc = new JFileChooser();
    int status = fc.showOpenDialog(this);
    File selectedFile = null;
    if (status == JFileChooser.APPROVE_OPTION) {
      selectedFile = fc.getSelectedFile();
    }
    return selectedFile;
  }

  private Vector<String> getPlanetSystemKeys() {
    Iterator<String> it = knownPlanetSystems.keys();
    Vector<String> vec = new Vector<String>();
    while (it.hasNext()) {
      vec.addElement(it.next());
    }
    return vec;
  }

  private boolean addSystem(DictionaryADT<String, StarSystem> target,
      String[] csv) {

    try {
      String systemName = csv[StarSystem.ColIndex.STAR_NAME.index()]
          .toUpperCase();

      StarSystem previous = null;
      if (target.contains(systemName)) {
        previous = target.getValue(systemName);
        target.delete(systemName);
      }
      StarSystem entry = StarSystem.updateFromArray(previous, csv);
      target.add(systemName, entry);
    } catch (Exception e) {

    }

    return true;
  }

  /**
   * Use the powers of polymorphism to drop in the BalancedTree and BST
   * implementations you provide.
   * 
   * @param csvFile
   * @return a new dictionary created from the input file
   */
  private DictionaryADT<String, StarSystem> buildDictionary(File csvFile) {

    DictionaryADT<String, StarSystem> dict = new HashTable<String, StarSystem>();
    // new BalancedTree<String, StarSystem>();
    //new BinarySearchTree<String, StarSystem>();

    BufferedReader br = null;
    String line = "";
    try {
      br = new BufferedReader(new FileReader(csvFile.getAbsolutePath()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }

    try {
      while ((line = br.readLine()) != null) {
        // # indicates comment, so move on if we found one
        if (line.startsWith("#")) {
          continue;
        }
        // Column description begins with characters
        if (Character.isLetter(line.charAt(0))) {
          continue;
        }
        // using comma as separator, add item to map
        addSystem(dict, line.split(","));
      }
    } catch (Exception e) {
      System.out.println("Wrong format File!");
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return dict;
  }

  JList<String> buildExoplanetList() {
    JList<String> list = new JList<String>();
    list.setVisibleRowCount(10);
    list.setBorder(new EmptyBorder(0, 0, 0, 0));

    Vector<String> keySet = getPlanetSystemKeys();
    list.setListData(keySet);

    systemList = list;

    return list;
  }

  JTextArea buildDescriptionArea() {
    JTextArea systemDescription = new JTextArea();
    systemDescription.setEditable(false);
    systemDescription.setFont(UIManager.getFont("EditorPane.font"));
    systemDescription.setBackground(UIManager.getColor("Panel.background"));
    systemDescription.setWrapStyleWord(true);
    systemDescription.setLineWrap(true);
    systemDescription.setText("Select an exoplanet star for a description.");

    return systemDescription;
  }

  private JPanel buildButtonPanel() {

    JPanel buttons = new JPanel();
    buttons.setBorder(new EmptyBorder(0, 0, 0, 0));
    buttons.setLayout(new BorderLayout(0, 0));

    JButton btnLoad = new JButton("Load");
    btnLoad.setAction(actionLoad);

    JButton btnExit = new JButton("Exit");
    btnExit.setAction(actionExit);

    buttons.add(btnLoad, BorderLayout.NORTH);
    buttons.add(btnExit, BorderLayout.SOUTH);

    return buttons;
  }

  private JPanel buildGui() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);

    JPanel panelSysDescription = new JPanel();
    panelSysDescription.setBorder(new EmptyBorder(0, 0, 0, 0));
    panelSysDescription.setLayout(new BorderLayout(0, 0));

    JTextArea txtrDescription = buildDescriptionArea();

    JScrollPane scrollPaneSysDescription = new JScrollPane(txtrDescription);
    scrollPaneSysDescription.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
    scrollPaneSysDescription.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    panelSysDescription.add(scrollPaneSysDescription, BorderLayout.CENTER);

    JLabel labelProperties = new JLabel("Properties:");
    labelProperties.setHorizontalAlignment(SwingConstants.LEFT);
    scrollPaneSysDescription.setColumnHeaderView(labelProperties);

    JList<String> exoplanetList = buildExoplanetList();
    JScrollPane scrollPaneStarNames = new JScrollPane(exoplanetList);
    JLabel lblExoplanetSystems = new JLabel("Exoplanet Systems:");
    lblExoplanetSystems.setHorizontalAlignment(SwingConstants.LEFT);
    scrollPaneStarNames.setColumnHeaderView(lblExoplanetSystems);

    JPanel panelStarNames = new JPanel();
    panelStarNames.setBorder(new EmptyBorder(0, 0, 0, 0));
    panelStarNames.setLayout(new BorderLayout(0, 0));
    panelStarNames.add(scrollPaneStarNames, BorderLayout.CENTER);

    exoplanetList.addListSelectionListener(
        new SystemSelected(exoplanetList, txtrDescription));

    JPanel pane = new JPanel();
    pane = new JPanel();
    pane.setBorder(new EmptyBorder(10, 10, 10, 10));
    pane.setLayout(new BorderLayout(0, 0));
    pane.add(panelStarNames, BorderLayout.WEST);
    pane.add(panelSysDescription);
    pane.add(buildButtonPanel(), BorderLayout.SOUTH);

    return pane;
  }

  public Exoplanets() {

    knownPlanetSystems = buildDictionary(selectCsvPlanetFile());
    setContentPane(buildGui());

    System.out.println("Complete!");
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Exoplanets frame = new Exoplanets();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

}
