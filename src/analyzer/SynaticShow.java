package analyzer;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SynaticShow extends JFrame {

  private JPanel contentPane;
  private JTable table_FirstAndFollow;
  private JTable table_Select;
  private JTable table_Predict;
  private JScrollPane scrollPane_GrammaTree;
  private JTextField textField_ProductionsFormat;
  private JTextField textField_TokenPath;
  private JTextField textField_ProductionShow;
  private JLabel label_1;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          SynaticShow frame = new SynaticShow();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public SynaticShow() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1230, 526);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);

    JScrollPane scrollPane_FirstAndFollow = new JScrollPane();
    scrollPane_FirstAndFollow.setBounds(10, 10, 266, 381);
    contentPane.add(scrollPane_FirstAndFollow);

    table_FirstAndFollow = new JTable();
    scrollPane_FirstAndFollow.setViewportView(table_FirstAndFollow);

    JScrollPane scrollPane_Select = new JScrollPane();
    scrollPane_Select.setBounds(286, 10, 286, 381);
    contentPane.add(scrollPane_Select);

    table_Select = new JTable();
    scrollPane_Select.setViewportView(table_Select);

    JScrollPane scrollPane_Predict = new JScrollPane();
    scrollPane_Predict.setBounds(582, 10, 286, 381);
    contentPane.add(scrollPane_Predict);

    table_Predict = new JTable();
    table_Predict.setFillsViewportHeight(true);
    scrollPane_Predict.setViewportView(table_Predict);

    scrollPane_GrammaTree = new JScrollPane();
    scrollPane_GrammaTree.setBounds(878, 10, 286, 381);
    contentPane.add(scrollPane_GrammaTree);

    JTextArea textArea_Tree = new JTextArea();
    scrollPane_GrammaTree.setViewportView(textArea_Tree);

    JButton button_Execute = new JButton("\u6267\u884C");
    button_Execute.setFont(new Font("楷体", Font.PLAIN, 18));
    button_Execute.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {


        Productions pros = new Productions(textField_ProductionsFormat.getText());
        PredictiveParsingTableConstructor prtc = new PredictiveParsingTableConstructor(pros);
        System.out.println(prtc.getPPTable().toString());
        SynaticAnalysisExecutor sAE = new SynaticAnalysisExecutor();
        MutiWayTree mwt = sAE.getGrammaTree(prtc.getPPTable(), textField_TokenPath.getText());

        // 变量准备-开始
        FirstSets first = prtc.getFirstSets();
        FollowSets follow = prtc.getFollowSets();

        SelectSets select = prtc.getSelectSets();

        Set<GrammarSymbol> firstGrammaSet = first.getGrammaSet();
        Set<GrammarSymbol> followGrammaSet = follow.getGrammaSet();

        Productions selectProductions = prtc.getPPTable().getProductions().breakIntoPieces();

        // 变量准备-结束

        // firstfollow-开始
        ArrayList<String[]> grammafirstfollowTable = new ArrayList<>();
        for (GrammarSymbol gs : firstGrammaSet) {
          String gramma = gs.getName();

          Set<GrammarSymbol> firstSet = first.getFirstSet(gs);
          String firstString = "";

          for (GrammarSymbol firstgs : firstSet) {
            firstString += firstgs.getName() + ",";
          }

          Set<GrammarSymbol> followSet = follow.getFollowSet(gs);
          String followString = "";

          for (GrammarSymbol followgs : followSet) {
            followString += followgs.getName() + ",";
          }
          String[] singleline = {gramma, firstString, followString};
          grammafirstfollowTable.add(singleline);

        }
        String[][] grammafirstfollowTableInput = new String[grammafirstfollowTable.size()][];
        grammafirstfollowTable.toArray(grammafirstfollowTableInput);
        String[] firstfollowcolumn = {"非终结符", "First集合", "Follow集合"};
        initialFirstAndFollow(table_FirstAndFollow, scrollPane_FirstAndFollow, firstfollowcolumn,
            grammafirstfollowTableInput);
        // firstfollow结束

        // select开始
        ArrayList<String[]> grammaSelectTable = new ArrayList<>();
        for (int i = 0; i < selectProductions.size(); i++) {
          Production production = selectProductions.getProduction(i);
          String selectlineString = "";
          String selectProductionString = production.getLHS().getName() + "-->";
          for (int j = 0; j < production.getRHSlist().get(0).size(); j++) {
            selectProductionString += production.getRHSlist().get(0).get(j).getName() + " ";
          }
          selectProductionString += "";

          Set<GrammarSymbol> selectSet = select.getSelectSet(production);
          for (GrammarSymbol selectgs : selectSet) {
            selectlineString += selectgs.getName() + ",";
          }
          String[] singleline = {selectProductionString, selectlineString};
          grammaSelectTable.add(singleline);
        }

        String[] selectcolumn = {"产生式", "Select集合"};
        String[][] grammaSelectTableInput = new String[grammaSelectTable.size()][];
        for (int i = 0; i < grammaSelectTable.size(); i++) {
          grammaSelectTableInput[i] = grammaSelectTable.get(i);
        }
        initialSelect(table_Select, scrollPane_Select, selectcolumn, grammaSelectTableInput);

        // select结束
        // table开始
        ArrayList<String[]> rowLineArrayList = new ArrayList<>();
        ArrayList<String> headList = new ArrayList<>();
        headList.add("  ");
        for (GrammarSymbol terminal : prtc.getPPTable().getColumnSet()) {
          headList.add(terminal.getName());
        }

        for (GrammarSymbol nonterminal : prtc.getPPTable().getRowSet()) {
          ArrayList<String> rowLine = new ArrayList<>();
          rowLine.add(nonterminal.getName());
          for (GrammarSymbol terminal : prtc.getPPTable().getColumnSet()) {
            String singleTableValue = "";

            // 填充单个表项
            if (!prtc.getPPTable().getProductionTableEntry(nonterminal, terminal)
                .equals(new Production())) {
              singleTableValue =
                  prtc.getPPTable().getProductionTableEntry(nonterminal, terminal).toString();
            } else {
              if (prtc.getPPTable().getSynchTableEntry(nonterminal, terminal)) {
                singleTableValue = "synch";
              } else {
                singleTableValue = "null";
              }

            }
            // 填充单个表项
            rowLine.add(singleTableValue);
          }
          String[] rowLineArray = new String[rowLine.size()];
          rowLine.toArray(rowLineArray);
          rowLineArrayList.add(rowLineArray);
        }


        String[][] rowLineArrayArray = new String[rowLineArrayList.size()][];
        rowLineArrayList.toArray(rowLineArrayArray);
        String[] headArray = new String[headList.size()];
        headList.toArray(headArray);
        initialFirstPredict(table_Predict, scrollPane_Predict, headArray, rowLineArrayArray,
            textField_ProductionShow);
        // table结束
        // tree开始
        String mwtString = mwt.toString();
        textArea_Tree.setText(mwtString);
        // tree结束



      }
    });
    button_Execute.setBounds(963, 430, 121, 39);
    contentPane.add(button_Execute);

    textField_ProductionsFormat = new JTextField();
    textField_ProductionsFormat.setText("src/data/input/productions1.format");
    textField_ProductionsFormat.setBounds(10, 440, 237, 21);
    contentPane.add(textField_ProductionsFormat);
    textField_ProductionsFormat.setColumns(10);

    textField_TokenPath = new JTextField();
    textField_TokenPath.setText("src/data/input/tokens.token");
    textField_TokenPath.setColumns(10);
    textField_TokenPath.setBounds(286, 440, 253, 21);
    contentPane.add(textField_TokenPath);

    JLabel label = new JLabel("\u4EA7\u751F\u5F0F\u6587\u4EF6");
    label.setFont(new Font("华文隶书", Font.PLAIN, 18));
    label.setBounds(75, 401, 132, 33);
    contentPane.add(label);

    JLabel lblToken = new JLabel("token\u6587\u4EF6");
    lblToken.setFont(new Font("华文隶书", Font.PLAIN, 18));
    lblToken.setBounds(326, 401, 132, 33);
    contentPane.add(lblToken);

    textField_ProductionShow = new JTextField();
    textField_ProductionShow.setColumns(10);
    textField_ProductionShow.setBounds(582, 440, 253, 21);
    contentPane.add(textField_ProductionShow);

    label_1 = new JLabel("\u8BE6\u7EC6\u4EA7\u751F\u5F0F");
    label_1.setFont(new Font("华文隶书", Font.PLAIN, 18));
    label_1.setBounds(582, 401, 132, 33);
    contentPane.add(label_1);
  }



  /**
   * 根据分析构建器的内容初始化FirstAndFollow表格
   * 
   * @param table
   * @param scrollPane
   */
  public void initialFirstAndFollow(JTable table, JScrollPane scrollPane, String[] columns,
      String[][] tableValues) {
    table = new JTable(tableValues, columns);
    scrollPane.setViewportView(table);
  }


  /**
   * 根据分析构建器的内容初始化Select表格
   * 
   * @param table
   * @param scrollPane
   */
  public void initialSelect(JTable table, JScrollPane scrollPane, String[] columns,
      String[][] tableValues) {

    table = new JTable(tableValues, columns);
    scrollPane.setViewportView(table);
  }


  /**
   * 根据分析构建器的内容初始化Predict表格
   * 
   * @param table
   * @param scrollPane
   */
  public void initialFirstPredict(JTable table, JScrollPane scrollPane, String[] columns,
      String[][] tableValues, JTextField show) {
    table = new JTable(tableValues, columns);
    scrollPane.setViewportView(table);
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if (o instanceof JTable) {
          JTable t = (JTable) o;
          String valueSelected = (String) t.getValueAt(t.getSelectedRow(), t.getSelectedColumn());
          show.setText(valueSelected);
        }

      }
    });
    table.getColumnModel().setColumnMargin(1);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

  }


}


