import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jess.QueryResult;
import jess.ValueVector;

public class Template extends JFrame{
	
	private JPanel topLayoutContainer;
	private JPanel topLayout;
	
	private JPanel botLayoutContainer;
	
	private JButton btnClose;
	private JTable tableMale, tableFemale;
	private Vector<String> maleCol, femaleCol;
	private Vector<Vector<String>> maleRow, femaleRow;
	
	private String skill, exp, fs, minAge, budget;
	private boolean isEmpty = true;
	
	private int WINDOW_WIDTH = 500;
	private int WINDOW_HEIGHT = 500;

	final JFrame frame = this;
	//ArrayList<String>e ;
	public Template(){
		init();
		initInfo();
		topLayouting();
		initData();
		botLayouting();
		add(btnClose = new JButton("Close"), BorderLayout.SOUTH);
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				//System.exit(0);
			}
		});
		
		setTitle("Avex Group - Artist");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initInfo(){
		try{
		/*================Set Query for Retrieve Artist Data=======================*/
		String query = "getInfo";
		/*=========================================================================*/
		QueryResult info = Main.rete.runQueryStar(query, 
				new ValueVector());
		if(info.next()){
			skill = info.getString("occ");
			exp = info.getString("exp");
			fs  = info.getString("fight");
			minAge = info.getString("age");
			budget = "$" + info.getString("pay");
		}
		}catch(Exception e){
			skill = exp = fs = minAge = budget = "Error";
			System.out.println(e);
		}
	}
	
	private void initData(){
		try {
			/*================Set Query for Retrieve Artist Data=======================*/
			String query = "artist-found";
			/*=========================================================================*/
			String Name, Age, Gender, Salary, Occupation, Experience, fs;
			QueryResult artist = Main.rete.runQueryStar(query, 
					new ValueVector());
			while(artist.next()){
				Name = artist.getString("name");
				Age  = artist.getString("age");
				Gender = artist.getString("gen");
				Salary = artist.getString("pay");
				Occupation = artist.getString("occ");
				Experience = artist.getString("exp");
				fs = artist.getString("fight");
				if(Gender.equals("Male"))
					insertMaleArtist(Name, Age, Salary, Occupation, Experience, fs);
				else if(Gender.equals("Female"))
					insertFemaleArtist(Name, Age, Salary, Occupation, Experience);
				
				isEmpty = false;
			}
		} catch (Exception e) {
			isEmpty = true;
			System.out.println(e);
		}
	}
	
	private void insertMaleArtist(String name, String age, String salary, String occupation, String experience, String skill){
		Vector<String> row = new Vector<String>();
		row.add(String.valueOf(maleRow.size()+1));
		row.add(name);
		row.add(age);
		row.add(salary);
		row.add(occupation);
		row.add(experience);
		row.add(skill);
		maleRow.add(row);
	}
	
	private void insertFemaleArtist(String name, String age, String salary, String occupation, String experience){
		Vector<String> row = new Vector<String>();
		row.add(String.valueOf(femaleRow.size()+1));
		row.add(name);
		row.add(age);
		row.add(salary);
		row.add(occupation);
		row.add(experience);
		femaleRow.add(row);
	}
	
	private void init(){
		initMaleCol();
		initFemaleCol();
		maleRow = new Vector<Vector<String>>();
		femaleRow = new Vector<Vector<String>>();
	}
	
	private void topLayouting(){
		topLayoutContainer = new JPanel(new BorderLayout());
		topLayout		   = new JPanel(new GridLayout(5, 3));
		
		topLayoutContainer.add(new JLabel("Artist Demand", JLabel.CENTER));
		topLayout.add(new JLabel("Skill"));
		topLayout.add(new JLabel(":"));
		topLayout.add(new JLabel(skill));
		topLayout.add(new JLabel("Experience"));
		topLayout.add(new JLabel(":"));
		topLayout.add(new JLabel(exp));
		topLayout.add(new JLabel("Require Fighting Skill"));
		topLayout.add(new JLabel(":"));
		topLayout.add(new JLabel(fs));
		topLayout.add(new JLabel("Minimal Age"));
		topLayout.add(new JLabel(":"));
		topLayout.add(new JLabel(minAge));
		topLayout.add(new JLabel("Budget"));
		topLayout.add(new JLabel(":"));
		topLayout.add(new JLabel(budget));
		
		topLayoutContainer.add(topLayout, BorderLayout.SOUTH);
		topLayoutContainer.setBorder(BorderFactory.createEmptyBorder(10,70,10,50));
		add(topLayoutContainer, BorderLayout.NORTH);
	}
	
	private void initMaleCol(){
		maleCol = new Vector<String>();
		maleCol.add("No.");
		maleCol.add("Name");
		maleCol.add("Age");
		maleCol.add("Salary");
		maleCol.add("Occupation");
		maleCol.add("Experience");
		maleCol.add("Fighting Skill");
	}
	
	private void initFemaleCol(){
		femaleCol = new Vector<String>();
		femaleCol.add("No.");
		femaleCol.add("Name");
		femaleCol.add("Age");
		femaleCol.add("Salary");
		femaleCol.add("Occupation");
		femaleCol.add("Experience");
	}
	
	private void botLayouting(){
		if(!isEmpty){
			DefaultTableModel dtmMale = new DefaultTableModel(maleRow, maleCol);
			DefaultTableModel dtmFemale = new DefaultTableModel(femaleRow, femaleCol);
			botLayoutContainer = new JPanel(new GridLayout(2, 1));
			botLayoutContainer.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
			JPanel tbMaleContainer = new JPanel(new BorderLayout());
			JPanel tbFemaleContainer = new JPanel(new BorderLayout());
			tbMaleContainer.add(new JLabel("Male Artist", JLabel.CENTER), BorderLayout.NORTH);
			tbFemaleContainer.add(new JLabel("Female Artist", JLabel.CENTER), BorderLayout.NORTH);
			tbMaleContainer.add(new JScrollPane(tableMale = new JTable(dtmMale)), BorderLayout.CENTER);
			tbFemaleContainer.add(new JScrollPane(tableFemale = new JTable(dtmFemale)), BorderLayout.CENTER);
			botLayoutContainer.add(tbMaleContainer);
			botLayoutContainer.add(tbFemaleContainer);
		}
		else{
			try {
				final Image image = ImageIO.read(new File("src/not_available.jpg"));
				botLayoutContainer = new JPanel(){
					@Override
					protected void paintComponent(Graphics g) {
						g.drawImage(image, WINDOW_WIDTH/2-200/2, 20, 200, 200, null);
					}
				};
			} catch (Exception e) {
				System.out.println("Image not found!");
			}
		}
		
		if(botLayoutContainer != null)
			add(botLayoutContainer, BorderLayout.CENTER);
	}
	
}