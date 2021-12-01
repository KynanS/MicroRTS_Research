

package gui.frontend;

import ai.BranchingFactorCalculatorBigInteger;
import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.ContinuingAI;
import ai.core.PseudoContinuingAI;
import ai.PassiveAI;
import ai.RandomAI;
import ai.RandomBiasedAI;
import ai.abstraction.HeavyDefense;
import ai.abstraction.HeavyRush;
import ai.abstraction.LightDefense;
import ai.abstraction.LightRush;
import ai.abstraction.RangedDefense;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerDefense;
import ai.abstraction.WorkerRush;
import ai.abstraction.WorkerRushPlusPlus;
import ai.abstraction.cRush.CRush_V1;
import ai.abstraction.cRush.CRush_V2;
import ai.abstraction.partialobservability.POHeavyRush;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.PORangedRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.BFSPathFinding;
import ai.abstraction.pathfinding.FloodFillPathFinding;
import ai.abstraction.pathfinding.GreedyPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.ahtn.AHTNAI;
import ai.core.ParameterSpecification;
import ai.evaluation.EvaluationFunction;
import ai.evaluation.EvaluationFunctionForwarding;
import ai.evaluation.SimpleEvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction2;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.informedmcts.InformedNaiveMCTS;
import ai.mcts.mlps.MLPSMCTS;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.mcts.uct.UCT;
import ai.mcts.uct.UCTFirstPlayUrgency;
import ai.mcts.uct.UCTUnitActions;
import ai.minimax.ABCD.IDABCD;
import ai.minimax.RTMiniMax.IDRTMinimax;
import ai.minimax.RTMiniMax.IDRTMinimaxRandomized;
import ai.montecarlo.MonteCarlo;
import ai.montecarlo.lsi.LSI;
import ai.portfolio.PortfolioAI;
import ai.portfolio.portfoliogreedysearch.PGSAI;
import ai.puppet.PuppetSearchMCTS;
import ai.stochastic.UnitActionProbabilityDistribution;
import gui.MouseController;
import gui.PhysicalGameStateMouseJFrame;
import gui.PhysicalGameStatePanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import java.io.Writer;

import rts.GameState;
import rts.PartiallyObservableGameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.PlayerActionGenerator;
import rts.Trace;
import rts.TraceEntry;
import rts.UnitAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;
import rts.units.UnitType;
import tests.MapGenerator;
import util.Pair;
import util.XMLWriter;
import ai.core.InterruptibleAI;
import ai.evaluation.SimpleOptEvaluationFunction;
import ai.mcts.believestatemcts.BS3_NaiveMCTS;
import ai.mcts.uct.DownsamplingUCT;
import ai.scv.SCV;

/**
 *
 * @author santi
 * Editor: kynan
 */
public class FEStatePane extends JPanel {
    
    PhysicalGameStatePanel statePanel;
    JTextArea textArea; // test are in GUI
    UnitTypeTable mainTable; // Table which all the units are stored.  Updated to hold best killer as games progress

    JFileChooser fileChooser = new JFileChooser();

    EvaluationFunction efs[] = {new SimpleEvaluationFunction(),
                                new SimpleSqrtEvaluationFunction(),
                                new SimpleSqrtEvaluationFunction2(),
                                new SimpleSqrtEvaluationFunction3(),
                                new EvaluationFunctionForwarding(new SimpleEvaluationFunction()),
                                new SimpleOptEvaluationFunction()};

    /**
     *
     */
    public static Class AIs[] = {
                   BS3_NaiveMCTS.class,
                   PassiveAI.class,
                   MouseController.class,
                   RandomAI.class,
                   RandomBiasedAI.class,
                   WorkerRush.class,
                   LightRush.class,
                   HeavyRush.class,
                   RangedRush.class,
                   WorkerDefense.class,
                   LightDefense.class,
                   HeavyDefense.class,
                   RangedDefense.class,
                   POWorkerRush.class,
                   POLightRush.class,
                   POHeavyRush.class,
                   PORangedRush.class,
                   WorkerRushPlusPlus.class,
                   CRush_V1.class,
                   CRush_V2.class,
                   PortfolioAI.class,
                   PGSAI.class,
                   IDRTMinimax.class,
                   IDRTMinimaxRandomized.class,
                   IDABCD.class,
                   MonteCarlo.class,
                   LSI.class,
                   UCT.class,
                   UCTUnitActions.class,
                   UCTFirstPlayUrgency.class,
                   DownsamplingUCT.class, 
                   NaiveMCTS.class,
                   
                   MLPSMCTS.class,
                   AHTNAI.class,
                   InformedNaiveMCTS.class,
                   PuppetSearchMCTS.class,
                   SCV.class
                  };

    
    Class PlayoutAIs[] = {
                   RandomAI.class,
                   RandomBiasedAI.class,
                   WorkerRush.class,
                   LightRush.class,
                   HeavyRush.class,
                   RangedRush.class,
                  };
    
    PathFinding pathFinders[] = {new AStarPathFinding(),
                                 new BFSPathFinding(),
                                 new GreedyPathFinding(),
                                 new FloodFillPathFinding()};
    
    public static UnitTypeTable unitTypeTables[] = {new UnitTypeTable(UnitTypeTable.VERSION_ORIGINAL, UnitTypeTable.MOVE_CONFLICT_RESOLUTION_CANCEL_BOTH),
                                      new UnitTypeTable(UnitTypeTable.VERSION_ORIGINAL, UnitTypeTable.MOVE_CONFLICT_RESOLUTION_CANCEL_ALTERNATING),
                                      new UnitTypeTable(UnitTypeTable.VERSION_ORIGINAL, UnitTypeTable.MOVE_CONFLICT_RESOLUTION_CANCEL_RANDOM),
                                      new UnitTypeTable(UnitTypeTable.VERSION_ORIGINAL_FINETUNED, UnitTypeTable.MOVE_CONFLICT_RESOLUTION_CANCEL_BOTH),
                                      new UnitTypeTable(UnitTypeTable.VERSION_ORIGINAL_FINETUNED, UnitTypeTable.MOVE_CONFLICT_RESOLUTION_CANCEL_ALTERNATING),
                                      new UnitTypeTable(UnitTypeTable.VERSION_ORIGINAL_FINETUNED, UnitTypeTable.MOVE_CONFLICT_RESOLUTION_CANCEL_RANDOM),
                                      new UnitTypeTable(UnitTypeTable.VERSION_NON_DETERMINISTIC, UnitTypeTable.MOVE_CONFLICT_RESOLUTION_CANCEL_BOTH),
                                      new UnitTypeTable(UnitTypeTable.VERSION_NON_DETERMINISTIC, UnitTypeTable.MOVE_CONFLICT_RESOLUTION_CANCEL_ALTERNATING),
                                      new UnitTypeTable(UnitTypeTable.VERSION_NON_DETERMINISTIC, UnitTypeTable.MOVE_CONFLICT_RESOLUTION_CANCEL_RANDOM),   
    };
    public static String unitTypeTableNames[] = {"Original-Both",
                                   "Original-Alternating",
                                   "Original-Random",
                                   "Finetuned-Both",
                                   "Finetuned-Alternating",
                                   "Finetuned-Random",
                                   "Nondeterministic-Both",
                                   "Nondeterministic-Alternating",
                                   "Nondeterministic-Random"};

    JFormattedTextField mapWidthField;
    JFormattedTextField mapHeightField;
    JFormattedTextField maxCyclesField;
    JFormattedTextField defaultDelayField;
    JFormattedTextField numberOfGames;
    JFormattedTextField numberOfLaps;
    JCheckBox fullObservabilityBox;
    JComboBox unitTypeTableBox;
    JCheckBox saveTraceBox;
    JCheckBox slowDownBox;
    
    JComboBox aiComboBox[] = {null,null};    
    JCheckBox continuingBox[] = {null,null};
    JPanel AIOptionsPanel[] = {null, null};
    HashMap AIOptionsPanelComponents[] = {new HashMap<String, JComponent>(), new HashMap<String, JComponent>()};   
    
    FEStateMouseListener mouseListener;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // variables and arrays that store important information to help test different versions of killer and determin results
    List<UnitType> killerOptions = new ArrayList<UnitType>();
    List<Runnable> unitTrials = new ArrayList<Runnable>();
    float bestScore = 0;
    UnitType bestKiller;
    //UnitType formerBest = (UnitType) mainTable.killer.clone();
    int laps = 0;
    int round = 1;
    int game = 1;
    float bestScoreR1 = 0;
    float bestScoreR2 = 0;
    int bestGamesMadeR1 = 0;
    int bestGamesWonWithR1 = 0;
    int bestGamesMade0 = 0;
    int bestGamesMade1 = 0;
    int bestGamesMadeBoth = 0;
    int bestGamesWonWith0 = 0;
    int bestGamesWonWith1 = 0;

    // variables for round 1
    float scoreR1[] = new float[10];
    int gamesWithKillerR1[] = new int[10];
    int totalKillerTime[] = new int[10];
    int gamesPlayedR1[] = new int[10];
    int gamesWonWithKillerR1[] = new int[10];

    // variables for round 2
    float scoreR2[] = new float[10];
    int gamesWithKiller0[] = new int[10];
    int gamesWithKiller1[] = new int[10];
    int gamesWithKillerBoth[] = new int[10];
    int gamesPlayedR2[] = new int[10];
    int gamesWonWithKiller0[] = new int[10];
    int gamesWonWithKiller1[] = new int[10];    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public FEStatePane() throws Exception {        

        mainTable = new UnitTypeTable();

        setLayout(new BorderLayout());

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        {
            JPanel ptmp = new JPanel();
            ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
            {
                JButton b = new JButton("Clear");
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.setAlignmentY(Component.TOP_ALIGNMENT);
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        GameState gs = statePanel.getState();
                        gs.getUnitActions().clear();
                        PhysicalGameState pgs = gs.getPhysicalGameState();
                        for(int i = 0;i<pgs.getHeight();i++) {
                            for(int j = 0;j<pgs.getWidth();j++) {
                                pgs.setTerrain(j,i,PhysicalGameState.TERRAIN_NONE);
                            }
                        }
                        pgs.getUnits().clear();
                        statePanel.repaint();
                    }
                });
                ptmp.add(b);
            }
            {
                JButton b = new JButton("Load");
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.setAlignmentY(Component.TOP_ALIGNMENT);
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        int returnVal = fileChooser.showOpenDialog((Component)null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            try {
                                PhysicalGameState pgs = PhysicalGameState.load(file.getAbsolutePath(), mainTable);
                                GameState gs = new GameState(pgs, mainTable);
                                statePanel.setStateDirect(gs);
                                statePanel.repaint();
                                mapWidthField.setText(pgs.getWidth()+"");
                                mapHeightField.setText(pgs.getHeight()+"");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                       }
                    }
                });
                ptmp.add(b);
            }
            {
                JButton b = new JButton("Save");
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.setAlignmentY(Component.TOP_ALIGNMENT);
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        if (statePanel.getGameState()!=null) {
                            int returnVal = fileChooser.showSaveDialog((Component)null);
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                File file = fileChooser.getSelectedFile();
                                try {
                                    XMLWriter xml = new XMLWriter(new FileWriter(file.getAbsolutePath()));
                                    statePanel.getGameState().getPhysicalGameState().toxml(xml);
                                    xml.flush();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                });
                ptmp.add(b);
            }
            p1.add(ptmp);
        }
        {
            JPanel ptmp = new JPanel();
            ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));        
            mapWidthField = addTextField(ptmp,"Width:", "8", 4);
            mapWidthField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        int newWidth = Integer.parseInt(mapWidthField.getText());
                        statePanel.resizeGameState(newWidth, statePanel.getGameState().getPhysicalGameState().getHeight());
                        statePanel.repaint();
                    } catch(Exception ex) {
                    }
                }            
            });
            mapHeightField = addTextField(ptmp,"Height:", "8", 4);
            mapHeightField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        int newHeight = Integer.parseInt(mapHeightField.getText());
                        statePanel.resizeGameState(statePanel.getGameState().getPhysicalGameState().getWidth(), newHeight);
                        statePanel.repaint();
                    } catch(Exception ex) {
                    }
                }            
            });
            p1.add(ptmp);
        }
        {
            JPanel ptmp = new JPanel();
            ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));        
            {
                JButton b = new JButton("Move Player 0");
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.setAlignmentY(Component.TOP_ALIGNMENT);
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        AI ai = createAI(aiComboBox[0].getSelectedIndex(), 0, mainTable);
                        if (ai instanceof MouseController) {
                            textArea.setText("Mouse controller is not allowed for this function.");
                            return;
                        }
                        try {
                            long start = System.currentTimeMillis();
                            ai.reset();
                            PlayerAction a = ai.getAction(0, statePanel.getGameState());
                            long end = System.currentTimeMillis();
                            textArea.setText("Action generated with " + ai.getClass().getSimpleName() + " in " + (end-start) + "ms\n");
                            textArea.append(ai.statisticsString() + "\n");
                            textArea.append("Action:\n");
                            for(Pair<Unit,UnitAction> tmp:a.getActions()) {
                                textArea.append("    " + tmp.m_a + ": " + tmp.m_b + "\n");
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                ptmp.add(b);
            }
            {
                JButton b = new JButton("Move Player 1");
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.setAlignmentY(Component.TOP_ALIGNMENT);
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        AI ai = createAI(aiComboBox[1].getSelectedIndex(), 1, mainTable);
                        if (ai instanceof MouseController) {
                            textArea.setText("Mouse controller is not allowed for this function.");
                            return;
                        }
                        try {
                            long start = System.currentTimeMillis();
                            ai.reset();
                            PlayerAction a = ai.getAction(0, statePanel.getGameState());
                            long end = System.currentTimeMillis();
                            textArea.setText("Action generated with " + ai.getClass().getSimpleName() + " in " + (end-start) + "ms\n");
                            textArea.append(ai.statisticsString() + "\n");
                            textArea.append("Action:\n");
                            for(Pair<Unit,UnitAction> tmp:a.getActions()) {
                                textArea.append("    " + tmp.m_a + ": " + tmp.m_b + "\n");
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                ptmp.add(b);
            }
            {
                JButton b = new JButton("Analyze");
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.setAlignmentY(Component.TOP_ALIGNMENT);
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        if (statePanel.getGameState()==null) {
                            textArea.setText("Load a game state first");
                            return;
                        }

                        try {
                            textArea.setText("");

                            // Evaluation functions:
                            textArea.append("Evaluation functions:\n");
                            for(EvaluationFunction ef:efs) {
                                textArea.append("  - " + ef.getClass().getSimpleName() + ": " + ef.evaluate(0, 1, statePanel.getGameState()) + ", " + ef.evaluate(1, 0, statePanel.getGameState()) + "\n");
                            }
                            textArea.append("\n");
                            
                            // units:
                            {
                                int n0 = 0, n1 = 0;
                                for(Unit u:statePanel.getGameState().getUnits()) {
                                    if (u.getPlayer()==0) n0++;
                                    if (u.getPlayer()==1) n1++;
                                }
                                textArea.append("Player 0 has " + n0 + " units\n");
                                textArea.append("Player 1 has " + n1 + " units\n\n");
                            }
                            textArea.append("Braching Factor (BigInteger):\n");
                            textArea.append("  - player 0: " + BranchingFactorCalculatorBigInteger.branchingFactorByResourceUsageSeparatingFast(statePanel.getGameState(), 0) + "\n");
                            textArea.append("  - player 1: " + BranchingFactorCalculatorBigInteger.branchingFactorByResourceUsageSeparatingFast(statePanel.getGameState(), 1) + "\n");
                            textArea.append("\n");

                            // Branching:
                            textArea.append("Unit moves:\n");
                            textArea.append("  - player 0:\n");
                            if (statePanel.getGameState().canExecuteAnyAction(0)) {
                                PlayerActionGenerator pag0 = new PlayerActionGenerator(statePanel.getGameState(), 0);
                                for(Pair<Unit,List<UnitAction>> tmp:pag0.getChoices()) {
                                    textArea.append("    " + tmp.m_a + " has " + tmp.m_b.size() + " actions: " + tmp.m_b + "\n");
                                }
                                textArea.append("\n");
                            }
                            textArea.append("  - player 1:\n");
                            if (statePanel.getGameState().canExecuteAnyAction(1)) {
                                PlayerActionGenerator pag1 = new PlayerActionGenerator(statePanel.getGameState(), 1);
                                for(Pair<Unit,List<UnitAction>> tmp:pag1.getChoices()) {
                                    textArea.append("    " + tmp.m_a + " has " + tmp.m_b.size() + " actions: " + tmp.m_b + "\n");
                                }
                                textArea.append("\n");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                ptmp.add(b);
            }            
            p1.add(ptmp);
        }
        {
            String colorSchemes[] = {"Color Scheme Black","Color Scheme White"};
            JComboBox b = new JComboBox(colorSchemes);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setAlignmentY(Component.TOP_ALIGNMENT);
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    JComboBox combo = (JComboBox)e.getSource();
                    if (combo.getSelectedIndex()==0) {
                        statePanel.setColorScheme(PhysicalGameStatePanel.COLORSCHEME_BLACK);
                    }
                    if (combo.getSelectedIndex()==1) {
                        statePanel.setColorScheme(PhysicalGameStatePanel.COLORSCHEME_WHITE);
                    }
                    statePanel.repaint();
                }
            });
            b.setMaximumSize(new Dimension(300,24));
            p1.add(b);
        }
        
        {
            JPanel ptmp = new JPanel();
            ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
            maxCyclesField = addTextField(ptmp,"Max Cycles:", "3000", 5);
            defaultDelayField = addTextField(ptmp,"Default Delay:", "10", 5);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // how many games you want to be played on each map for each version of killer
            numberOfGames = addTextField(ptmp, "Games:", "1", 5);
            // each lap tests all possible neighbors of the current best unit as found by the score
            // each lap takes the best unit from the previous lap and tries each of it's neighbors
            numberOfLaps = addTextField(ptmp, "Laps", "1", 5);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            p1.add(ptmp);
        }
        {
            JPanel ptmp = new JPanel();
            ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
            {
                fullObservabilityBox = new JCheckBox("Full Obsservability");
                fullObservabilityBox.setSelected(true);
                fullObservabilityBox.setAlignmentX(Component.CENTER_ALIGNMENT);
                fullObservabilityBox.setAlignmentY(Component.TOP_ALIGNMENT);
                fullObservabilityBox.setMaximumSize(new Dimension(120,20));
                fullObservabilityBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        statePanel.setFullObservability(fullObservabilityBox.isSelected());
                        statePanel.repaint();
                    }
                });
                ptmp.add(fullObservabilityBox);
            }
            {
                slowDownBox = new JCheckBox("Slow Down");
                slowDownBox.setAlignmentX(Component.CENTER_ALIGNMENT);
                slowDownBox.setAlignmentY(Component.TOP_ALIGNMENT);
                slowDownBox.setMaximumSize(new Dimension(120,20));
                slowDownBox.setSelected(false);
                ptmp.add(slowDownBox);
            }
            p1.add(ptmp);
        }
        {
            JPanel ptmp = new JPanel();
            ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
            ptmp.add(new JLabel("UnitTypeTable"));
            unitTypeTableBox = new JComboBox(unitTypeTableNames);
            unitTypeTableBox.setAlignmentX(Component.CENTER_ALIGNMENT);
            unitTypeTableBox.setAlignmentY(Component.CENTER_ALIGNMENT);
            unitTypeTableBox.setMaximumSize(new Dimension(240,20));
            unitTypeTableBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int idx = unitTypeTableBox.getSelectedIndex();
                    UnitTypeTable new_utt = unitTypeTables[idx];
                    GameState gs = statePanel.getGameState().cloneChangingUTT(new_utt);
                    if (gs!=null) {
                        statePanel.setStateDirect(gs);
                        mainTable = new_utt;
                        mouseListener.utt = new_utt;
                    } else {
                        System.err.println("Could not change unit type table!");
                    }
                }
            });
            ptmp.add(unitTypeTableBox);
            p1.add(ptmp);
        }
        {
            JPanel ptmp = new JPanel();
            ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Start of Games
            {
                
                JButton b = new JButton("Start");
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.setAlignmentY(Component.TOP_ALIGNMENT);
                b.setMaximumSize(new Dimension(120,20));
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {     
                        // begin the first lap of games.  All further actions will be done within setUpLap and the functions it calls           
                        setUpLap();
                    }
                });
                ptmp.add(b);
            }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            {
                saveTraceBox = new JCheckBox("Save Trace");
                saveTraceBox.setAlignmentX(Component.CENTER_ALIGNMENT);
                saveTraceBox.setAlignmentY(Component.TOP_ALIGNMENT);
                saveTraceBox.setMaximumSize(new Dimension(120,20));
                ptmp.add(saveTraceBox);
            }
            p1.add(ptmp);
        }
        
       
                
        for(int player = 0;player<2;player++) {
            p1.add(new JSeparator(SwingConstants.HORIZONTAL));
            {
                JPanel ptmp = new JPanel();
                ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
                JLabel l1 = new JLabel("Player "+player+":");
                l1.setAlignmentX(Component.CENTER_ALIGNMENT);
                l1.setAlignmentY(Component.TOP_ALIGNMENT);
                ptmp.add(l1);
                String AINames[] = new String[AIs.length];
                for(int i = 0;i<AIs.length;i++) {
                    AINames[i] = AIs[i].getSimpleName();
                }
                aiComboBox[player] = new JComboBox(AINames);
                aiComboBox[player].setAlignmentX(Component.CENTER_ALIGNMENT);
                aiComboBox[player].setAlignmentY(Component.TOP_ALIGNMENT);
                aiComboBox[player].setMaximumSize(new Dimension(300,24));
                ptmp.add(aiComboBox[player]);
                p1.add(ptmp);
            }
            continuingBox[player] = new JCheckBox("Continuing");
            continuingBox[player].setAlignmentX(Component.CENTER_ALIGNMENT);
            continuingBox[player].setAlignmentY(Component.TOP_ALIGNMENT);
            continuingBox[player].setMaximumSize(new Dimension(120,20));
            continuingBox[player].setSelected(true);
            p1.add(continuingBox[player]);
            
            AIOptionsPanel[player] = new JPanel();
            AIOptionsPanel[player].setLayout(new BoxLayout(AIOptionsPanel[player], BoxLayout.Y_AXIS));
            p1.add(AIOptionsPanel[player]);

            updateAIOptions(AIOptionsPanel[player], player);
        }
        
        aiComboBox[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateAIOptions(AIOptionsPanel[0], 0);
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
        aiComboBox[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateAIOptions(AIOptionsPanel[1], 1);
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });   
        
//        p1.add(Box.createVerticalGlue());
        MapGenerator mg = new MapGenerator(mainTable);
        GameState initialGs = new GameState(mg.bases8x8(), mainTable);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        statePanel = new PhysicalGameStatePanel(initialGs);
        statePanel.setPreferredSize(new Dimension(512, 512));
        p2.add(statePanel);
        textArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        scrollPane.setPreferredSize(new Dimension(512, 192));
        p2.add(scrollPane, BorderLayout.CENTER);

        add(p1, BorderLayout.WEST);
        add(p2, BorderLayout.EAST);
        
        mouseListener = new FEStateMouseListener(statePanel, mainTable);
        statePanel.addMouseListener(mouseListener);              
    }    

    public void setState(GameState gs) {
        statePanel.setStateDirect(gs);
        statePanel.repaint();
        mapWidthField.setText(gs.getPhysicalGameState().getWidth()+"");
        mapHeightField.setText(gs.getPhysicalGameState().getHeight()+"");
    }        
    
    private static String nextTraceName() {
        int idx = 1;
        do {
            String name = "trace" + idx + ".xml";
            File f = new File(name);
            if (!f.exists()) return name;
            idx++;
        }while(true);
    }    
    
    public static JFormattedTextField addTextField(JPanel p, String name, String defaultValue, int columns) {
        JPanel ptmp = new JPanel();
        ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
        ptmp.add(new JLabel(name));
        JFormattedTextField f = new JFormattedTextField();
        f.setValue(defaultValue);
//        f.setColumns(columns);
        f.setMaximumSize(new Dimension(80,20));
        ptmp.add(f);
        p.add(ptmp);
        return f;
    }

    public AI createAI(int idx, int player, UnitTypeTable utt) {
        try {
            AI ai = createAIInternal(idx, player, utt);

            // set parameters:
            List<ParameterSpecification> parameters = ai.getParameters();
            for(ParameterSpecification p:parameters) {
                if (p.type == int.class) {
                    JFormattedTextField f = (JFormattedTextField)AIOptionsPanelComponents[player].get(p.name);
                    int v = Integer.parseInt(f.getText());
                    Method setter = ai.getClass().getMethod("set" + p.name, p.type);
                    setter.invoke(ai, v);
                    
                } else if (p.type == long.class) {
                    JFormattedTextField f = (JFormattedTextField)AIOptionsPanelComponents[player].get(p.name);
                    long v = Long.parseLong(f.getText());
                    Method setter = ai.getClass().getMethod("set" + p.name, p.type);
                    setter.invoke(ai, v);
                    
                } else if (p.type == float.class) {
                    JFormattedTextField f = (JFormattedTextField)AIOptionsPanelComponents[player].get(p.name);
                    float v = Float.parseFloat(f.getText());
                    Method setter = ai.getClass().getMethod("set" + p.name, p.type);
                    setter.invoke(ai, v);
                    
                } else if (p.type == double.class) {
                    JFormattedTextField f = (JFormattedTextField)AIOptionsPanelComponents[player].get(p.name);
                    double v = Double.parseDouble(f.getText());
                    Method setter = ai.getClass().getMethod("set" + p.name, p.type);
                    setter.invoke(ai, v);
                    
                } else if (p.type == String.class) {
                    JFormattedTextField f = (JFormattedTextField)AIOptionsPanelComponents[player].get(p.name);
                    Method setter = ai.getClass().getMethod("set" + p.name, p.type);
                    setter.invoke(ai, f.getText());

                } else if (p.type == boolean.class) {
                    JCheckBox f = (JCheckBox)AIOptionsPanelComponents[player].get(p.name);
                    Method setter = ai.getClass().getMethod("set" + p.name, p.type);
                    setter.invoke(ai, f.isSelected());
                    
                } else {
                    JComboBox f = (JComboBox)AIOptionsPanelComponents[player].get(p.name);
                    Method setter = ai.getClass().getMethod("set" + p.name, p.type);
                    setter.invoke(ai, f.getSelectedItem());
                }
            }

            if (continuingBox[player].isSelected()) {
                // If the user wants a "continuous" AI, check if we can wrap it around a continuing decorator:
                if (ai instanceof AIWithComputationBudget) {
                    if (ai instanceof InterruptibleAI) {
                        ai = new ContinuingAI(ai);
                    } else {
                        ai = new PseudoContinuingAI((AIWithComputationBudget)ai);        				
                    }
                }
            }
            return ai;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public AI createAIInternal(int idx, int player, UnitTypeTable utt) throws Exception {

        if (AIs[idx]==MouseController.class) {
            return new MouseController(null);
        } else {
            Constructor cons = AIs[idx].getConstructor(UnitTypeTable.class);
            AI AI_instance = (AI)cons.newInstance(utt);

            return AI_instance;
        }
    }

    private void updateAIOptions(JPanel jPanel, int player) throws Exception {
        // clear previous components:
        HashMap<String, JComponent> components = AIOptionsPanelComponents[player];
        jPanel.removeAll();
        components.clear();
        
        AI AIInstance = createAIInternal(aiComboBox[player].getSelectedIndex(), 0, mainTable);
        List<ParameterSpecification> parameters = AIInstance.getParameters();
        for(ParameterSpecification p:parameters) {
            if (p.type == int.class ||
                p.type == long.class ||
                p.type == float.class ||
                p.type == double.class ||
                p.type == String.class) {
                JComponent c = addTextField(jPanel,p.name, p.defaultValue.toString(), p.defaultValue.toString().length()+1);
                components.put(p.name, c);
                
            } else if (p.type == boolean.class) {
                JCheckBox c = new JCheckBox(p.name);
                c.setAlignmentX(Component.CENTER_ALIGNMENT);
                c.setAlignmentY(Component.TOP_ALIGNMENT);
                c.setMaximumSize(new Dimension(120,20));
                c.setSelected((Boolean)p.defaultValue);
                jPanel.add(c);
                components.put(p.name, c);

            } else if (p.type == PathFinding.class) {
                JPanel ptmp = new JPanel();
                ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
                ptmp.add(new JLabel(p.name));
                int defaultValue = 0;
                
                PathFinding PFSNames[] = new PathFinding[pathFinders.length];
                for(int i = 0;i<pathFinders.length;i++) {
                    PFSNames[i] = pathFinders[i];
                    if (pathFinders[i].getClass() == p.defaultValue.getClass()) defaultValue = i;
                }
                JComboBox c = new JComboBox(PFSNames);
                c.setAlignmentX(Component.CENTER_ALIGNMENT);
                c.setAlignmentY(Component.TOP_ALIGNMENT);
                c.setMaximumSize(new Dimension(300,24));
                c.setSelectedIndex(defaultValue);
                
                ptmp.add(c);
                jPanel.add(ptmp);
                components.put(p.name, c);
                
            } else if (p.type == EvaluationFunction.class) {
                JPanel ptmp = new JPanel();
                ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
                ptmp.add(new JLabel(p.name));
                int defaultValue = 0;
                
                EvaluationFunction EFSNames[] = new EvaluationFunction[efs.length];
                for(int i = 0;i<efs.length;i++) {
                    EFSNames[i] = efs[i];
                    if (efs[i].getClass() == p.defaultValue.getClass()) defaultValue = i;
                }
                JComboBox c = new JComboBox(EFSNames);
                c.setAlignmentX(Component.CENTER_ALIGNMENT);
                c.setAlignmentY(Component.TOP_ALIGNMENT);
                c.setMaximumSize(new Dimension(300,24));
                c.setSelectedIndex(defaultValue);
                
                ptmp.add(c);
                jPanel.add(ptmp);
                components.put(p.name, c);

            } else if (p.type == AI.class) {
                // we are assuming this is a simple playout AI (so, a smaller list is used here):
                JPanel ptmp = new JPanel();
                ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
                ptmp.add(new JLabel(p.name));
                int defaultValue = 0;
                
                AI AINames[] = null;
                if (p.possibleValues==null) {                
                    AINames= new AI[PlayoutAIs.length];
                    for(int i = 0;i<PlayoutAIs.length;i++) {
                        AINames[i] = (AI)PlayoutAIs[i].getConstructor(UnitTypeTable.class).newInstance(mainTable);
                        if (PlayoutAIs[i] == p.defaultValue.getClass()) defaultValue = i;
                    }
                } else {
                    AINames = new AI[p.possibleValues.size()];
                    for(int i = 0;i<p.possibleValues.size();i++) {
                        AINames[i] = (AI)p.possibleValues.get(i);
                        if (p.possibleValues.get(i) == p.defaultValue) defaultValue = i;
                    }
                }
                JComboBox c = new JComboBox(AINames);
                c.setAlignmentX(Component.CENTER_ALIGNMENT);
                c.setAlignmentY(Component.TOP_ALIGNMENT);
                c.setMaximumSize(new Dimension(300,24));
                c.setSelectedIndex(defaultValue);
               
                ptmp.add(c);
                jPanel.add(ptmp);
                components.put(p.name, c);
            
            } else if (p.type == UnitActionProbabilityDistribution.class) {
                JPanel ptmp = new JPanel();
                ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
                ptmp.add(new JLabel(p.name));
                int defaultValue = 0;
                
                UnitActionProbabilityDistribution names[] = null;
                names= new UnitActionProbabilityDistribution[p.possibleValues.size()];
                for(int i = 0;i<p.possibleValues.size();i++) {
                    names[i] = (UnitActionProbabilityDistribution)p.possibleValues.get(i);
                    if (p.possibleValues.get(i) == p.defaultValue) defaultValue = i;
                }
                JComboBox c = new JComboBox(names);
                c.setAlignmentX(Component.CENTER_ALIGNMENT);
                c.setAlignmentY(Component.TOP_ALIGNMENT);
                c.setMaximumSize(new Dimension(300,24));
                c.setSelectedIndex(defaultValue);
               
                ptmp.add(c);
                jPanel.add(ptmp);
                components.put(p.name, c);                

            } else if (p.possibleValues!=null) {
                JPanel ptmp = new JPanel();
                ptmp.setLayout(new BoxLayout(ptmp, BoxLayout.X_AXIS));
                ptmp.add(new JLabel(p.name));
                int defaultValue = 0;
                
                Object []options = new Object[p.possibleValues.size()];
                for(int i = 0;i<p.possibleValues.size();i++) {
                    options[i] = p.possibleValues.get(i);
                    if (p.possibleValues.get(i).equals(p.defaultValue)) defaultValue = i;
                }
                JComboBox c = new JComboBox(options);
                c.setAlignmentX(Component.CENTER_ALIGNMENT);
                c.setAlignmentY(Component.TOP_ALIGNMENT);
                c.setMaximumSize(new Dimension(300,24));
                c.setSelectedIndex(defaultValue);
               
                ptmp.add(c);
                jPanel.add(ptmp);
                components.put(p.name, c);
                
            } else {
                throw new Exception("Cannot create GUI component for class" + p.type.getName());
            }
        }
        
        jPanel.revalidate();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // return a runnable object with a specific version of the new unit
    public Runnable makeRunnable(int versionNum){
        Runnable r = new Runnable() {  
            public void run() {
                try {
                    
                    int rVersion = versionNum;
                    boolean player0 = false;
                    boolean player1 = false;
                    mainTable.killer = killerOptions.get(versionNum);
                    AI ai1 = createAI(aiComboBox[0].getSelectedIndex(), 0, mainTable);
                    AI ai2 = createAI(aiComboBox[1].getSelectedIndex(), 1, mainTable);
                    int PERIOD1 = Integer.parseInt(defaultDelayField.getText());
                    int PERIOD2 = Integer.parseInt(defaultDelayField.getText());
                    JFormattedTextField t1 = (JFormattedTextField)AIOptionsPanelComponents[0].get("TimeBudget");
                    JFormattedTextField t2 = (JFormattedTextField)AIOptionsPanelComponents[1].get("TimeBudget");
                    if (t1!=null) PERIOD1 = Integer.parseInt(t1.getText());
                    if (t2!=null) PERIOD2 = Integer.parseInt(t2.getText());
                    
                    int PERIOD = PERIOD1 + PERIOD2;
                    if (!slowDownBox.isSelected()) {
                        PERIOD = 1;
                    }
                    int MAXCYCLES = Integer.parseInt(maxCyclesField.getText());
                    GameState gs = statePanel.getState().clone();
                    
                    ai1.preGameAnalysis(gs, -1);
                    ai2.preGameAnalysis(gs, -1);
                    
                    boolean gameover = false;
                    List<Integer> killerAppear = new ArrayList<Integer>();
                    List<Integer> killerDisappear = new ArrayList<Integer>();
                    boolean wasKillerMade = false;
                    int totalGameTime = 0;
                    JFrame w = null;
                    Trace trace = null;
                    if (saveTraceBox.isSelected()) {
                        trace = new Trace(mainTable);
                        TraceEntry te = new TraceEntry(gs.getPhysicalGameState().clone(),gs.getTime());
                        trace.addEntry(te);                        
                    }

                    boolean isMouseController = false;
                    if (ai1 instanceof MouseController) isMouseController = true;
                    if (ai2 instanceof MouseController) isMouseController = true;
                    if ((ai1 instanceof PseudoContinuingAI) && (((PseudoContinuingAI)ai1).getbaseAI() instanceof MouseController)) isMouseController = true;
                    if ((ai2 instanceof PseudoContinuingAI) && (((PseudoContinuingAI)ai2).getbaseAI() instanceof MouseController)) isMouseController = true;
                    
                    if (isMouseController) {
                        PhysicalGameStatePanel pgsp = new PhysicalGameStatePanel(statePanel);
                        pgsp.setStateDirect(gs);
                        w = new PhysicalGameStateMouseJFrame("Game State Visualizer (Mouse)",640,640,pgsp);
                        
                        boolean mousep1 = false;
                        boolean mousep2 = false;
                        if (ai1 instanceof MouseController) {
                            ((MouseController)ai1).setFrame((PhysicalGameStateMouseJFrame)w);
                            mousep1 = true;
                        } else if ((ai1 instanceof PseudoContinuingAI) && (((PseudoContinuingAI)ai1).getbaseAI() instanceof MouseController)) {
                            ((MouseController)((PseudoContinuingAI)ai1).getbaseAI()).setFrame((PhysicalGameStateMouseJFrame)w);
                            mousep1 = true;
                        }
                        if (ai2 instanceof MouseController) {
                            ((MouseController)ai2).setFrame((PhysicalGameStateMouseJFrame)w);
                            mousep2 = true;
                        } else if ((ai2 instanceof PseudoContinuingAI) && (((PseudoContinuingAI)ai2).getbaseAI() instanceof MouseController)) {
                            ((MouseController)((PseudoContinuingAI)ai2).getbaseAI()).setFrame((PhysicalGameStateMouseJFrame)w);
                            mousep2 = true;
                        }
                        if (mousep1 && !mousep2) pgsp.setDrawFromPerspectiveOfPlayer(0);
                        if (!mousep1 && mousep2) pgsp.setDrawFromPerspectiveOfPlayer(1);
                    } else {
                        w = PhysicalGameStatePanel.newVisualizer(gs,640,640,!fullObservabilityBox.isSelected(),statePanel.getColorScheme());
                    }
                    
                    long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
                    do{
                        if (System.currentTimeMillis()>=nextTimeToUpdate) {
                            PlayerAction pa1 = null;
                            PlayerAction pa2 = null;
                            if (fullObservabilityBox.isSelected()) {
                                pa1 = ai1.getAction(0, gs);
                                pa2 = ai2.getAction(1, gs);
                            } else {
                                pa1 = ai1.getAction(0, new PartiallyObservableGameState(gs,0));
                                pa2 = ai2.getAction(1, new PartiallyObservableGameState(gs,1));
                            }
                            if (trace!=null && (!pa1.isEmpty() || !pa2.isEmpty())) {
                                TraceEntry te = new TraceEntry(gs.getPhysicalGameState().clone(),gs.getTime());
                                te.addPlayerAction(pa1.clone());
                                te.addPlayerAction(pa2.clone());
                                trace.addEntry(te);
                            }
                            synchronized(gs) {
                                gs.issueSafe(pa1);
                                gs.issueSafe(pa2);
                            }

                            // check to see if a Killer was made for round 1
                            if (round == 1) {
                                if (gs.getPhysicalGameState().killerMade() && !wasKillerMade) {
                                    if (killerAppear.isEmpty()) {
                                        gamesWithKillerR1[rVersion]++;
                                    } 
                                    killerAppear.add(gs.getTime());                                                            
                                    wasKillerMade = true;                                            
                                }  
                                // check to see if killer was killed
                                if (!gs.getPhysicalGameState().killerMade() && wasKillerMade) {                             
                                    killerDisappear.add(gs.getTime());
                                    wasKillerMade = false;
                                }
                            }      
                            
                            // check to see if killer was made for round 2
                            if (round == 2) {
                                List<Unit> killers = gs.getPhysicalGameState().killerTeam();
                                if (!killers.isEmpty()) {
                                    for (Unit u : killers) {
                                        if (u.player == 0 && !player0) {
                                            gamesWithKiller0[rVersion]++;
                                            player0 = true;
                                        }
                                        if (u.player == 1 && !player1) {
                                            gamesWithKiller1[rVersion]++;
                                            player1 = true;
                                        }
                                        if (player0 && player1) {
                                            gamesWithKillerBoth[rVersion]++;
                                        }
                                    }
                                }
                            }
                            
                            // simulate:
                            synchronized(gs) {
                                gameover = gs.cycle();
                            }
                            if (gs.getTime() >= Integer.parseInt(maxCyclesField.getText())) {
                                gameover = true;
                            }
                            w.repaint();
                            nextTimeToUpdate+=PERIOD;
                        } else {
                            Thread.sleep(1);
                        }
                        if (!w.isVisible()) break;  // if the user has closed the window
                    }while(!gameover && gs.getTime()<MAXCYCLES);

                    if (gameover){
                        // close the window
                        w.dispose();
                        textArea.append(game + " ");
                        game++;
                        // game over steps for round 1
                        if (round == 1) {
                            killerDisappear.add(gs.getTime());
                            totalGameTime += gs.getTime();
                            gamesPlayedR1[rVersion]++;

                            // add to the score if player 0 won and killer was made
                            if (gs.getPhysicalGameState().winner() == 0 && !killerAppear.isEmpty()) {
                                for (int i = 0; i < killerAppear.size(); i++) {                                
                                    scoreR1[rVersion] += 1f * FinalScore(killerDisappear.get(i).floatValue(), killerAppear.get(i).floatValue(), gs.getTime());                                    
                                }   
                                gamesWonWithKillerR1[rVersion]++;
                            }

                            // subract to the score if player 1 won/there was a draw and killer was made
                            else if (!killerAppear.isEmpty()) {
                                for (int i = 0; i < killerAppear.size(); i++) {                                
                                    scoreR1[rVersion] -= 1f * FinalScore(killerDisappear.get(i).floatValue(), killerAppear.get(i).floatValue(), gs.getTime());
                                }
                            }

                            // don't add to the score if killer wasn't made regardless of win, loss or draw
                            // if all the games for this unit in this round have been played, record the results
                            if (gamesPlayedR1[rVersion] == Integer.parseInt(numberOfGames.getText())){
                                String testFilePath = "C:\\Users\\kynan\\Documents\\OneDrive\\rtsResearch\\MicroRTS\\tests.csv";
                                File testFile = new File(testFilePath);
                                scoreR1[rVersion] = scoreR1[rVersion] / (gamesPlayedR1[rVersion]);
                                try {
                                    Writer testOutputFile = new FileWriter(testFile, true);
                                    testOutputFile.append("Killer has cost: "+ killerOptions.get(versionNum).cost + " hp: " + killerOptions.get(versionNum).hp + " min Damage: " + killerOptions.get(versionNum).minDamage + " max Damage: " + killerOptions.get(versionNum).maxDamage + " attack range: " + killerOptions.get(versionNum).attackRange + "\n");
                                    testOutputFile.write("The number of games where killer was made was: " + gamesWithKillerR1[rVersion] + "\n");
                                    testOutputFile.write("The number of games won by Player 0 with killer: " + gamesWonWithKillerR1[rVersion] + "\n");
                                    testOutputFile.write("The score was: " + scoreR1[rVersion] + "\n");
                                    testOutputFile.close();                                
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            // check to see if it's time to start round 2
                            boolean round2 = true;
                            for (int i = 0; i < 10; i++) {
                                if (gamesPlayedR1[i] < Integer.parseInt(numberOfGames.getText())) {
                                    round2 = false;
                                }
                            }
                            if (round2) {
                                round = 2;
                                setUpLap();
                            } 
                        }

                        if (round == 2) {
                            gamesPlayedR2[rVersion]++;
                            // if player 0 wins and a player made killer
                            if (gs.getPhysicalGameState().winner() == 0 && (player0 || player1)) {
                                gamesWonWithKiller0[rVersion]++;
                            }
                            // if player 1 wins and a player made killer
                            if (gs.getPhysicalGameState().winner() == 1 && (player0 || player1)) {
                                gamesWonWithKiller1[rVersion]++;
                            }

                            // record findings
                            if (gamesPlayedR2[rVersion] == Integer.parseInt(numberOfGames.getText())){
                                String testFilePath = "C:\\Users\\kynan\\Documents\\OneDrive\\rtsResearch\\MicroRTS\\tests.csv";
                                File testFile = new File(testFilePath);
                                Integer gw0 = gamesWonWithKiller0[rVersion];
                                Integer gwk0 = gamesWithKiller0[rVersion];
                                Integer gwk1 = gamesWithKiller1[rVersion];
                                scoreR2[rVersion] = 1f - Math.abs(0.5f - (gw0.floatValue()/(gwk0.floatValue() + gwk1.floatValue())));
                                try {
                                    Writer testOutputFile = new FileWriter(testFile, true);
                                    testOutputFile.append("Killer has cost: "+ killerOptions.get(versionNum).cost + " hp: " + killerOptions.get(versionNum).hp + " min Damage: " + killerOptions.get(versionNum).minDamage + " max Damage: " + killerOptions.get(versionNum).maxDamage + " attack range: " + killerOptions.get(versionNum).attackRange + "\n");
                                    testOutputFile.write("The number of games where killer was made by player 0 was: " + gamesWithKiller0[rVersion] + "\n");
                                    testOutputFile.write("The number of games where killer was made by player 1 was: " + gamesWithKiller1[rVersion] + "\n");
                                    testOutputFile.write("The number of games where killer was made by both players was: " + gamesWithKillerBoth[rVersion] + "\n");
                                    testOutputFile.write("The number of games won by Player 0 with killer: " + gamesWonWithKiller0[rVersion] + "\n");
                                    testOutputFile.write("The number of games won by Player 1 with killer: " + gamesWonWithKiller1[rVersion] + "\n");
                                    testOutputFile.write("The score was: " + scoreR2[rVersion] + "\n");
                                    testOutputFile.write("\n");
                                    testOutputFile.close();                                
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            boolean eval = true;
                            for (int i = 0; i < 10; i++) {
                                if (gamesPlayedR2[i] < Integer.parseInt(numberOfGames.getText())) {
                                    eval = false;
                                }
                            }
                            if (eval) {
                                textArea.append("\n");
                                evaluate();
                            }
                        }
                    }
                    
                    if (trace!=null) {
                        TraceEntry te = new TraceEntry(gs.getPhysicalGameState().clone(), gs.getTime());
                        trace.addEntry(te);                
                        String traceFileName = FEStatePane.nextTraceName();                        
                        textArea.append("Trace saved as " + traceFileName + "\n");
                        XMLWriter xml = new XMLWriter(new FileWriter(traceFileName));
                        trace.toxml(xml);
                        xml.flush();
                    }   
                }                                 
                    
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        return r;
    }

    //set things up to run a lap of tests
    public void setUpLap(){
        try {
            MovingCompany(mainTable.killer);
        } catch (CloneNotSupportedException e1) {
            e1.printStackTrace();
        }
        String FilePath = "C:\\Users\\kynan\\Documents\\OneDrive\\rtsResearch\\microrts-master\\microrts-master\\maps\\Standard\\Standard1";
        if (round == 1) {
            Arrays.fill(scoreR1, 0);
            Arrays.fill(gamesWithKillerR1, 0);
            Arrays.fill(totalKillerTime, 0);
            Arrays.fill(gamesPlayedR1, 0);
            Arrays.fill(gamesWonWithKillerR1, 0); 
            FilePath = "C:\\Users\\kynan\\Documents\\OneDrive\\rtsResearch\\microrts-master\\microrts-master\\maps\\Standard\\Standard1";
        } else if (round == 2) {
            FilePath = "C:\\Users\\kynan\\Documents\\OneDrive\\rtsResearch\\microrts-master\\microrts-master\\maps\\Standard\\Standard3";
            Arrays.fill(scoreR2, 0);
            Arrays.fill(gamesPlayedR2, 0);
            Arrays.fill(gamesWithKiller0, 0);
            Arrays.fill(gamesWithKiller1, 0);
            Arrays.fill(gamesWithKillerBoth, 0);
            Arrays.fill(gamesWonWithKiller0, 0); 
            Arrays.fill(gamesWonWithKiller1, 0); 
        }
        
        File file = new File(FilePath);
        try {
            PhysicalGameState pgs = PhysicalGameState.load(file.getAbsolutePath(), mainTable);
            GameState gs = new GameState(pgs, mainTable);
            statePanel.setStateDirect(gs);
            statePanel.repaint();
            mapWidthField.setText(pgs.getWidth()+"");
            mapHeightField.setText(pgs.getHeight()+"");
        } catch (Exception ex) {
            ex.printStackTrace();
        }       
        ExecutorService trialRuns = Executors.newFixedThreadPool(10);
                                                                            
        //all the different versions of killer in their own thread so as to be able to run and test them all
        Runnable trial0 = makeRunnable(0);  
        unitTrials.add(trial0);
        Runnable trial1 = makeRunnable(1);
        unitTrials.add(trial1);
        Runnable trial2 = makeRunnable(2);
        unitTrials.add(trial2);
        Runnable trial3 = makeRunnable(3);
        unitTrials.add(trial3);
        Runnable trial4 = makeRunnable(4);
        unitTrials.add(trial4);
        Runnable trial5 = makeRunnable(5);
        unitTrials.add(trial5);
        Runnable trial6 = makeRunnable(6);
        unitTrials.add(trial6);
        Runnable trial7 = makeRunnable(7);
        unitTrials.add(trial7);
        Runnable trial8 = makeRunnable(8);
        unitTrials.add(trial8);
        Runnable trial9 = makeRunnable(9);
        unitTrials.add(trial9);
                
        for (int j = 0; j < 10; j++){
            for (int k = 0; k < Integer.parseInt(numberOfGames.getText()); k++){
                trialRuns.execute(unitTrials.get(j));
            }            
        }
        trialRuns.shutdown();  
    }

    //check for an improved unit
    public void evaluate() throws CloneNotSupportedException{
        laps++;
        textArea.append(laps + " laps done\n");
        float newScore;

        for (int i = 0; i < 10; i++){
            newScore = scoreR1[i] + scoreR2[i];
            if (newScore > bestScore) {
                //formerBest = (UnitType) bestKiller.clone();
                bestKiller = killerOptions.get(i);
                bestScore = newScore;
                bestScoreR1 = scoreR1[i];
                bestScoreR2 = scoreR2[i];
                bestGamesMadeR1 = gamesWithKillerR1[i];
                bestGamesWonWithR1 = gamesWonWithKillerR1[i];
                bestGamesMade0 = gamesWithKiller0[i];
                bestGamesMade1 = gamesWithKiller1[i];
                bestGamesMadeBoth = gamesWithKillerBoth[i];
                bestGamesWonWith0 = gamesWonWithKiller0[i];
                bestGamesWonWith1 = gamesWonWithKiller1[i];
            }            
        }

        try {
            mainTable.killer = (UnitType) bestKiller.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        String testFilePath = "C:\\Users\\kynan\\Documents\\OneDrive\\rtsResearch\\MicroRTS\\BestUnits.csv";
        File testFile = new File(testFilePath);
        try {
            Writer testOutputFile = new FileWriter(testFile, true);
            testOutputFile.write("After " + laps + " laps have occured, the best unit found is: \n");
            testOutputFile.write("Killer has cost: "+ bestKiller.cost + " hp: " + bestKiller.hp + " min Damage: " + bestKiller.minDamage + " max Damage: " + bestKiller.maxDamage + " attack range: " + bestKiller.attackRange + "\n");
            testOutputFile.write("The total score was : " + bestScore + "\n");
            testOutputFile.write("The score for round 1 was: " + bestScoreR1 + "\n");
            testOutputFile.write("The unit was made in: " + bestGamesMadeR1 + " games" + "\n");
            testOutputFile.write("The unit was made in and was won with in : " + bestGamesWonWithR1 + " games" + "\n");
            testOutputFile.write("The score for round 2 was: " + bestScoreR2 + "\n");
            testOutputFile.write("The unit was made by player 0 in: " + bestGamesMade0 + " games" + "\n");
            testOutputFile.write("The unit was made by player 1 in: " + bestGamesMade1 + " games" + "\n");
            testOutputFile.write("The unit was made by both players in the same game in: " + bestGamesMadeBoth + " games" + "\n");
            testOutputFile.write("The unit was made in and was won with by player 0 in : " + bestGamesWonWith0 + " games" + "\n");
            testOutputFile.write("The unit was made in and was won with by player 1 in : " + bestGamesWonWith1 + " games" + "\n");
            testOutputFile.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // make sure there was an improvement
        /*
        if (formerBest == bestKiller) {
            laps = Integer.parseInt(numberOfLaps.getText());
            textArea.append("No better neighbors\n");
        }
        */
        if (laps == Integer.parseInt(numberOfLaps.getText())){
            textArea.append("laps are complete\n");
            return;
        }
        round = 1;
        setUpLap();  
    }

    //make a list of all possible new units for a cycle of tests
    public void MovingCompany(UnitType inputKiller) throws CloneNotSupportedException{
        killerOptions.clear();
        //different versions
        UnitType temp0 = (UnitType)inputKiller.clone();
        UnitType temp1 = (UnitType)inputKiller.clone();    
        UnitType temp2 = (UnitType)inputKiller.clone();
        UnitType temp3 = (UnitType)inputKiller.clone();
        UnitType temp4 = (UnitType)inputKiller.clone();
        UnitType temp5 = (UnitType)inputKiller.clone();
        UnitType temp6 = (UnitType)inputKiller.clone();
        UnitType temp7 = (UnitType)inputKiller.clone();
        UnitType temp8 = (UnitType)inputKiller.clone();
        UnitType temp9 = (UnitType)inputKiller.clone();

        //for cost
        temp0.cost += 1;
        killerOptions.add(temp0);
        if ((inputKiller.cost - 1) >= 0){
            temp1.cost -= 1;            
        }
        killerOptions.add(temp1);

        //for hp
        temp2.hp += 1;
        killerOptions.add(temp2);
        if ((inputKiller.hp - 1) > 0){
            temp3.hp -= 1;            
        }
        killerOptions.add(temp3);
          
        //for min damage
        temp4.minDamage += 1;
        if (temp4.minDamage > inputKiller.maxDamage){
            temp4.maxDamage += 1;
        }
        killerOptions.add(temp4);
        if ((inputKiller.minDamage - 1) >= 0){
            temp5.minDamage -= 1;            
        }
        killerOptions.add(temp5);        

        //for max damage
        temp6.maxDamage += 1;
        killerOptions.add(temp6);
        if ((inputKiller.maxDamage - 1) >= inputKiller.minDamage){
            temp7.maxDamage -= 1;
        } else{
            temp7.maxDamage -= 1;
            temp7.minDamage -= 1;
        }
        killerOptions.add(temp7);        

        //for attack range
        temp8.attackRange += 1;
        killerOptions.add(temp8);
        if ((inputKiller.attackRange - 1) > 0){
            temp9.attackRange -= 1;
        }
        killerOptions.add(temp9);

    }

    // used to determine the score.  Easy to update when put here
    float FinalScore(float appear, float disappear, int time) {

        float score = (disappear - appear) / time;

        return score;
    }
}
