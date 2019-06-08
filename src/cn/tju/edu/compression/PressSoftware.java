package cn.tju.edu.compression;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PressSoftware {
	private JFrame frame;
	private JPanel mainPanel;
	private JTextField htimeTextField;
	private JTextField ltimeTextField;
	private JTextField hcompressibilityTextField;
	private JTextField lcompressibilityTextField;
	private JTextField sourceTextField;
	private JTextField destinationTextField;
	
	private HuffmanCompression compressionWay1;
	private LZ77Compression compressionWay2;
	
	private String sourceFilePath;
	private String destinationFilePath;
	
	public PressSoftware() {
		this.compressionWay1 = new HuffmanCompression(this);
		this.compressionWay2 = new LZ77Compression(this);
		setInterface();
	}
	
	public void setInterface() {
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(null);
		
		JLabel label = new JLabel("中国最蠢    压缩软件");
		label.setFont(new Font("华文行楷", Font.PLAIN, 23));
		label.setBounds(100, 7, 220, 30);
		mainPanel.add(label);
		
		JLabel htimeLabel = new JLabel("霍耗时");
		htimeLabel.setFont(new Font("楷体", Font.PLAIN, 16));
		htimeLabel.setBounds(30, 40, 55, 22);
		mainPanel.add(htimeLabel);
		
		htimeTextField = new JTextField();
		htimeTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		htimeTextField.setColumns(10);
		htimeTextField.setBounds(85, 42, 70, 22);
		htimeTextField.setEditable(false);
		mainPanel.add(htimeTextField);
		
		JLabel hcompressibilityLabel = new JLabel("压缩率");
		hcompressibilityLabel.setFont(new Font("楷体", Font.PLAIN, 16));
		hcompressibilityLabel.setBounds(170, 40, 55, 22);
		mainPanel.add(hcompressibilityLabel);
		
		hcompressibilityTextField = new JTextField();
		hcompressibilityTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		hcompressibilityTextField.setColumns(10);
		hcompressibilityTextField.setBounds(240, 42, 70, 22);
		hcompressibilityTextField.setEditable(false);
		mainPanel.add(hcompressibilityTextField);
		
		JLabel ltimeLabel = new JLabel("LZ耗时");
		ltimeLabel.setFont(new Font("楷体", Font.PLAIN, 16));
		ltimeLabel.setBounds(32, 70, 55, 22);
		mainPanel.add(ltimeLabel);
		
		ltimeTextField = new JTextField();
		ltimeTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		ltimeTextField.setBounds(85, 72, 70, 22);
		ltimeTextField.setColumns(10);
		ltimeTextField.setEditable(false);
		mainPanel.add(ltimeTextField);
		
		JLabel lcompressibilityLabel = new JLabel("压缩率");
		lcompressibilityLabel.setFont(new Font("楷体", Font.PLAIN, 16));
		lcompressibilityLabel.setBounds(170, 70, 55, 22);
		mainPanel.add(lcompressibilityLabel);
		
		lcompressibilityTextField = new JTextField();
		lcompressibilityTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		lcompressibilityTextField.setColumns(10);
		lcompressibilityTextField.setBounds(240, 72, 70, 22);
		lcompressibilityTextField.setEditable(false);
		mainPanel.add(lcompressibilityTextField);
		
		JLabel sourceLabel = new JLabel("源路径");
		sourceLabel.setFont(new Font("楷体", Font.PLAIN, 16));
		sourceLabel.setBounds(30, 100, 55, 22);
		mainPanel.add(sourceLabel);
		
		sourceTextField = new JTextField();
		sourceTextField.setFont(new Font("楷体", Font.PLAIN, 12));
		sourceTextField.setBounds(85, 102, 150, 22);
		sourceTextField.setColumns(10);
		sourceTextField.setEditable(false);
		mainPanel.add(sourceTextField);
		
		JLabel destinationLabel = new JLabel("末路径");
		destinationLabel.setFont(new Font("楷体", Font.PLAIN, 16));
		destinationLabel.setBounds(30, 125, 55, 25);
		mainPanel.add(destinationLabel);
		
		destinationTextField = new JTextField();
		destinationTextField.setFont(new Font("楷体", Font.PLAIN, 12));
		destinationTextField.setColumns(10);
		destinationTextField.setBounds(85, 127, 150, 22);
		destinationTextField.setEditable(false);
		mainPanel.add(destinationTextField);
		
		JButton hcompressionButton = new JButton("压缩");
		hcompressionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(sourceFilePath == null) {
					sourceTextField.setText("Choose a source file path!");
					destinationTextField.setText("");
				}
				else {
					if(destinationFilePath == null) {
						destinationFilePath = sourceFilePath.split("\\.")[0].concat(".cpn");
						destinationTextField.setText(destinationFilePath);
					}
					compressionWay1.setDestinationFilePath(destinationFilePath);
					compressionWay1.writeFile();
					sourceTextField.setText("Compression is finished!");
				}
				sourceFilePath = null;
				destinationFilePath = null;
			}
		});
		hcompressionButton.setForeground(Color.MAGENTA);
		hcompressionButton.setFont(new Font("楷体", Font.PLAIN, 16));
		hcompressionButton.setBounds(330, 41, 67, 22);
		mainPanel.add(hcompressionButton);
		
		JButton hdecompressionButton = new JButton("解压");
		hdecompressionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(sourceFilePath == null) {
					sourceTextField.setText("Choose a source file path!");
					destinationTextField.setText("");
				}
				else{
					if(sourceFilePath.endsWith(".cpn")) {
						if(destinationFilePath == null) {
							destinationFilePath = sourceFilePath.split("\\.")[0].concat(".txt");
							destinationTextField.setText(destinationFilePath);
						}
						compressionWay1.setDestinationFilePath(destinationFilePath);
						compressionWay1.decompression();
						sourceTextField.setText("Decompression is finished!");
					}
					else {
						sourceTextField.setText("Please choose a file with suffix cpn!");
					}
				}
				sourceFilePath = null;
				destinationFilePath = null;
			}
		});
		hdecompressionButton.setForeground(Color.MAGENTA);
		hdecompressionButton.setFont(new Font("楷体", Font.PLAIN, 16));
		hdecompressionButton.setBounds(330, 101, 67, 22);
		mainPanel.add(hdecompressionButton);
		
		JButton lcompressionButton = new JButton("压缩");
		lcompressionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(sourceFilePath == null) {
					sourceTextField.setText("Choose a source file path!");
					destinationTextField.setText("");
				}
				else {
					if(destinationFilePath == null) {
						destinationFilePath = sourceFilePath.split("\\.")[0].concat(".cpn");
						destinationTextField.setText(destinationFilePath);
					}
					compressionWay2.setDestinationFilePath(destinationFilePath);
					compressionWay2.writeFile();
					sourceTextField.setText("Compression is finished!");
				}
				sourceFilePath = null;
				destinationFilePath = null;
			}
		});
		lcompressionButton.setForeground(Color.ORANGE);
		lcompressionButton.setFont(new Font("楷体", Font.PLAIN, 16));
		lcompressionButton.setBounds(330, 71, 67, 22);
		mainPanel.add(lcompressionButton);

		JButton ldecompressionButton = new JButton("解压");
		ldecompressionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(sourceFilePath == null) {
					sourceTextField.setText("Choose a source file path!");
					destinationTextField.setText("");
				}
				else{
					if(sourceFilePath.endsWith(".cpn")) {
						if(destinationFilePath == null) {
							destinationFilePath = sourceFilePath.split("\\.")[0].concat(".txt");
							destinationTextField.setText(destinationFilePath);
						}
						compressionWay2.setDestinationFilePath(destinationFilePath);
						compressionWay2.decompression();
						sourceTextField.setText("Decompression is finished!");
					}
					else {
						sourceTextField.setText("Please choose a file with suffix cpn!");
					}
				}
				sourceFilePath = null;
				destinationFilePath = null;
			}
		});
		ldecompressionButton.setForeground(Color.ORANGE);
		ldecompressionButton.setFont(new Font("楷体", Font.PLAIN, 16));
		ldecompressionButton.setBounds(330, 128, 67, 22);
		mainPanel.add(ldecompressionButton);
		
		JButton sourceButton = new JButton("选择");
		sourceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				htimeTextField.setText("");
				ltimeTextField.setText("");
				hcompressibilityTextField.setText("");
				lcompressibilityTextField.setText("");
				sourceTextField.setText("");
				destinationTextField.setText("");
				JFileChooser sourceFileChooser = new JFileChooser();
				int judge = sourceFileChooser.showOpenDialog(null);
				if(judge == sourceFileChooser.APPROVE_OPTION) {
					sourceFilePath = sourceFileChooser.getSelectedFile().getAbsolutePath();
					compressionWay1.setSourceFilePath(sourceFilePath);
					compressionWay2.setSourceFilePath(sourceFilePath);
					sourceTextField.setText(sourceFilePath);
					if(!sourceFilePath.endsWith(".cpn")) {
						Thread thread1 = new Thread(compressionWay1);
						Thread thread2 = new Thread(compressionWay2);
						thread1.start();
						thread2.start();
					}
				}
			}
		});
		sourceButton.setFont(new Font("楷体", Font.PLAIN, 16));
		sourceButton.setBounds(243, 101, 67, 22);
		mainPanel.add(sourceButton);
		
		JButton destinationButton = new JButton("选择");
		destinationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JFileChooser destinationFileChooser = new JFileChooser();
				int judge = destinationFileChooser.showOpenDialog(null);
				if(judge == destinationFileChooser.APPROVE_OPTION) {
					destinationFilePath = destinationFileChooser.getSelectedFile().getAbsolutePath();
					destinationTextField.setText(destinationFilePath);
				}
			}
		});
		destinationButton.setFont(new Font("楷体", Font.PLAIN, 16));
		destinationButton.setBounds(243, 128, 67, 22);
		mainPanel.add(destinationButton);
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setContentPane(mainPanel);
	}
	
	public void addHTimeTextField(String htime) {
		this.htimeTextField.setText(htime);
	}
	
	public void addLTimeTextField(String ltime) {
		this.ltimeTextField.setText(ltime);
	}
	
	public void addHCompressibilityTextField(String hcompressibility) {
		this.hcompressibilityTextField.setText(hcompressibility);
	}
	
	public void addLCompressibilityTextField(String lcompressibility) {
		this.lcompressibilityTextField.setText(lcompressibility);
	}

	public static void main(String args[]) {
		PressSoftware pressSoftware = new PressSoftware();
	}
}
