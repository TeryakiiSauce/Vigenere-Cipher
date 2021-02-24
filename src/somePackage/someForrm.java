/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package somePackage;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author elias rahma
 * @since Aug 15 2020, Saturday @ 12:00 A.M.
 * @finished Aug 31 2020, Monday @ 10:10 P.M.
 * @version 1.1a
 * @dateUpdated Sep 13 2020, Sunday @ 10:13 P.M.
 * @enjoymentWorkingWithThisProject 11/10! (excluding the hard parts lol) :)
 * 
 * WHAT I'VE LEARNT:
 * 
 * 1. [ System.out.println(); ] ACTUALLY WASTES TIME!! | post link: https://stackoverflow.com/questions/16784773/does-system-out-println-effect-the-code-efficiency
 * 2. THREADS ARE A HEADACHE!!!
 * 3. PROGRESS BAR CALCULATIONS ARE NOT FOR ME! well i still did it so i'm kinda proud after all the rage i've released the other day lol
 * 4. COMING SOON...
 */
public class someForrm extends javax.swing.JFrame {
    private final static Logger LOGGER = Logger.getLogger(someForrm.class.getName()); // for logging and outputting results extremely fast (way faster than [ System.out.println(); ] )
    
    // =====================================
    // ||                                 ||
    // ||  Waiting Dots Animation Thread  ||
    // ||                                 ||
    // =====================================
    
    /**
     * VOLATILE KEYWORD EXPLAINATION [source: https://www.geeksforgeeks.org/killing-threads-in-java/]
     * A situation may arise when more than one threads are accessing the same variable and the changes made by one
     * might not be visible to other threads. In such a situation, we can use a volatile boolean flag.
     * 
     * ALSO THE THREADING PART WAS SO HARD TO DO, IM GLAD I FINALLY DID IT! IT WAS FREAKIN' CONFUSINGGG
     * 
     * Detailed info of what went wrong:
     * 
     * Whenever i tried to stop and kill the thread it used to take somewhat long to kill it completely
     * (like a second or so, which is too much *awkward moon face*).
     * Not only that but sometimes it also used to show "waiting for process..." in the result text box at the moment of killing the thread
     * instead of the decrypted or encrypted text.
     * 
     * like: 1. "waiting for process..."
     *       2. "the result text" -> then after few millis it changes back to...
     *       3. "waiting for process..."
     * 
     * the killing thread code was written in the [processBtnActionPerformed] method
     * 
     * then i realized that since it takes sometime to kill the thread, it would be better to just kill the thread whenever
     * a user enters some text in the [message] & [key] text boxes
     * 
     * this way there will be enough time for the thread to terminate before updating the [resultText] to the new encrypted or decrypted text
     * before the thread finally dies it will change the [resultText] to "click the button to continue" from "waiting for input..."
     * letting the user know that they can finally click the button without problems
     * 
     * might not be the best way but if it works, it works idc (as of now atleast)
     */
    private volatile boolean processFinished = false;
    public class WaitingDots implements Runnable {
        private volatile boolean busterCallStatus = false; // real One Piece fans would know
        
        public synchronized void requestBusterCall() {
            this.busterCallStatus = true;
        }
        
        public synchronized boolean busterCallIsRequested() {
            return this.busterCallStatus;
        }
        
        public void sleep(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Thread was interrupted, more info:\n", e);
            }
        }
        
        @Override
        public void run() {
            LOGGER.log(Level.INFO, "Waiting dots thread started running...");
            while (!busterCallIsRequested() && !processFinished) {
                resultText.setText("\n"
                        + "\n"
                        + "\n"
                        + "\n"
                        + "     waiting for input        ");
                sleep(750);
                
                resultText.setText("\n"
                        + "\n"
                        + "\n"
                        + "\n"
                        + "     waiting for input.       ");
                sleep(750);
                
                resultText.setText("\n"
                        + "\n"
                        + "\n"
                        + "\n"
                        + "     waiting for input..      ");
                sleep(750);
                
                resultText.setText("\n"
                        + "\n"
                        + "\n"
                        + "\n"
                        + "     waiting for input...     ");
                sleep(750);
            }
            
            resultText.setText("\n"
                    + "\n"
                    + "\n"
                    + "\n"
                    + "   click button to proceed ");
            LOGGER.log(Level.INFO, "Buster call was initiated therefore the thread was destroyed!");
        }
    }
    
    // ===========================
    // ||                       ||
    // ||  Form Initialization  ||
    // ||                       ||
    // ===========================
    
    /**
     * Creates new form someForrm
     */
    public someForrm() {
        initComponents();
        message.requestFocus();
        try {
            titleLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("VictorMono-BoldItalic_(title).ttf")).deriveFont(28f));
            versionLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("VictorMono-LightItalic_(version).ttf")).deriveFont(14f));
            messageLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("VictorMono-SemiBoldItalic_(labels).ttf")).deriveFont(14f));
            message.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("Berlin Sans FB Regular (message).ttf")).deriveFont(15f));
            keyLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("VictorMono-SemiBoldItalic_(labels).ttf")).deriveFont(14f));
            key.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("SpaceMono-BoldItalic_(key).ttf")).deriveFont(23f));
            processBtn.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("iosevka-ss07-extrabolditalic_(processBtn).ttf")).deriveFont(18f));
            charCounterLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("MajorMonoDisplay-Regular_(charCounterLabel).ttf")).deriveFont(13f));
            resultText.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("SourceCodePro-Regular_(result).ttf")).deriveFont(14f));
            websiteLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("VictorMono-LightItalic_(version).ttf")).deriveFont(11f));
            copyrightLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("VictorMono-Regular_(copyright).ttf")).deriveFont(11f));
            saveBtn.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("iosevka-ss07-extrabolditalic_(processBtn).ttf")).deriveFont(10f));
            progressBar.setFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("VictorMono-SemiBoldItalic_(labels).ttf")).deriveFont(12f));
            
        } catch (IOException | FontFormatException e) {
            LOGGER.log(Level.SEVERE, "Either file not found or file format not supported, more info:\n", e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        messageLabel = new javax.swing.JLabel();
        messageSep = new javax.swing.JSeparator();
        messagePanel = new javax.swing.JScrollPane();
        message = new javax.swing.JTextArea();
        keyLabel = new javax.swing.JLabel();
        keySep = new javax.swing.JSeparator();
        key = new javax.swing.JTextField();
        exitBtn = new javax.swing.JLabel();
        minimizeBtn = new javax.swing.JLabel();
        processBtnPanel = new javax.swing.JPanel();
        processBtn = new javax.swing.JButton();
        togglePanel = new javax.swing.JPanel();
        decryptionToggle = new javax.swing.JToggleButton();
        charCounterSep = new javax.swing.JSeparator();
        charCounterSep.setVisible(false);
        charCounterLabel = new javax.swing.JLabel();
        charCounterLabel.setVisible(false);
        rightPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        titleBottomSep = new javax.swing.JLabel();
        titleTopSep = new javax.swing.JLabel();
        titleMiddleSep = new javax.swing.JLabel();
        lockIcon = new javax.swing.JLabel();
        resultTextPanel = new javax.swing.JScrollPane();
        resultText = new javax.swing.JTextArea();
        twitterLogo = new javax.swing.JLabel();
        githubLogo = new javax.swing.JLabel();
        websiteLabel = new javax.swing.JLabel();
        copyrightLabel = new javax.swing.JLabel();
        saveBtnPanel = new javax.swing.JPanel();
        saveBtnPanel.setVisible(false);
        saveBtn = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        progressBar.setVisible(false);
        timeTakenLabel = new javax.swing.JLabel();
        timeTakenLabel.setVisible(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Vigenère Cipher");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(500, 130));
        setMinimumSize(new java.awt.Dimension(720, 520));
        setUndecorated(true);
        setSize(new java.awt.Dimension(720, 520));

        mainPanel.setBackground(new java.awt.Color(63, 63, 63));
        mainPanel.setForeground(new java.awt.Color(60, 63, 65));
        mainPanel.setMaximumSize(new java.awt.Dimension(720, 520));
        mainPanel.setPreferredSize(new java.awt.Dimension(720, 520));
        mainPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mainPanelMouseDragged(evt);
            }
        });
        mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mainPanelMousePressed(evt);
            }
        });
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        leftPanel.setBackground(new java.awt.Color(63, 63, 63));
        leftPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(140, 234, 234), 10));
        leftPanel.setMaximumSize(new java.awt.Dimension(370, 480));
        leftPanel.setMinimumSize(new java.awt.Dimension(370, 480));
        leftPanel.setPreferredSize(new java.awt.Dimension(370, 480));
        leftPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        messageLabel.setBackground(new java.awt.Color(0, 0, 0));
        messageLabel.setFont(new java.awt.Font("Victor Mono SemiBold", 0, 14)); // NOI18N
        messageLabel.setForeground(new java.awt.Color(216, 216, 216));
        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageLabel.setLabelFor(message);
        messageLabel.setText("enter text (english only)");
        messageLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        messageLabel.setMaximumSize(new java.awt.Dimension(250, 35));
        messageLabel.setMinimumSize(new java.awt.Dimension(250, 35));
        messageLabel.setPreferredSize(new java.awt.Dimension(250, 35));
        leftPanel.add(messageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 250, 35));

        messageSep.setMaximumSize(new java.awt.Dimension(230, 10));
        messageSep.setMinimumSize(new java.awt.Dimension(230, 10));
        messageSep.setPreferredSize(new java.awt.Dimension(230, 10));
        leftPanel.add(messageSep, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 230, 10));

        messagePanel.setMaximumSize(new java.awt.Dimension(289, 165));
        messagePanel.setMinimumSize(new java.awt.Dimension(289, 165));
        messagePanel.setPreferredSize(new java.awt.Dimension(289, 165));

        message.setBackground(new java.awt.Color(68, 68, 68));
        message.setColumns(10);
        message.setFont(new java.awt.Font("Berlin Sans FB", 0, 15)); // NOI18N
        message.setForeground(new java.awt.Color(204, 204, 204));
        message.setLineWrap(true);
        message.setToolTipText("<html>\n  </head>\n    <style>\n      p {color: #993366; font-family: sans-serif;}\n    </style>\n  </head>\n\n  <body>\n    <p>enter your secret message to encrypt using the vigenere cipher hehe ^-^</p>\n  </body>\n</html>");
        message.setWrapStyleWord(true);
        message.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 255, 255), new java.awt.Color(204, 255, 255), new java.awt.Color(0, 153, 153), new java.awt.Color(0, 153, 153)));
        message.setCaretColor(new java.awt.Color(255, 127, 63));
        message.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        message.setMargin(new java.awt.Insets(10, 10, 10, 10));
        message.setMaximumSize(new java.awt.Dimension(104, 26));
        message.setSelectedTextColor(new java.awt.Color(102, 102, 102));
        message.setSelectionColor(new java.awt.Color(175, 238, 238));
        message.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                messageFocusLost(evt);
            }
        });
        message.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                messageKeyReleased(evt);
            }
        });
        messagePanel.setViewportView(message);

        leftPanel.add(messagePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 289, 165));

        keyLabel.setBackground(new java.awt.Color(0, 0, 0));
        keyLabel.setFont(new java.awt.Font("Victor Mono SemiBold", 0, 14)); // NOI18N
        keyLabel.setForeground(new java.awt.Color(216, 216, 216));
        keyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        keyLabel.setLabelFor(key);
        keyLabel.setText("enter key value");
        keyLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        keyLabel.setMaximumSize(new java.awt.Dimension(290, 35));
        keyLabel.setMinimumSize(new java.awt.Dimension(290, 35));
        keyLabel.setPreferredSize(new java.awt.Dimension(290, 35));
        leftPanel.add(keyLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, 150, 35));

        keySep.setMaximumSize(new java.awt.Dimension(150, 10));
        keySep.setMinimumSize(new java.awt.Dimension(150, 10));
        keySep.setPreferredSize(new java.awt.Dimension(150, 10));
        leftPanel.add(keySep, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 310, 150, 10));

        key.setBackground(new java.awt.Color(68, 68, 68));
        key.setFont(new java.awt.Font("Space Mono", 3, 23)); // NOI18N
        key.setForeground(new java.awt.Color(204, 204, 204));
        key.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        key.setToolTipText("<html>   </head>     <style>       p {color: #993366; font-family: sans-serif;}     </style>   </head>    <body>     <p>enter a passcode so you can secure it</p>     <p>make sure to remember it though!</p>   </body> </html>");
        key.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 255, 255), new java.awt.Color(204, 255, 255), new java.awt.Color(0, 153, 153), new java.awt.Color(0, 153, 153)));
        key.setCaretColor(new java.awt.Color(255, 127, 63));
        key.setMaximumSize(new java.awt.Dimension(124, 35));
        key.setMinimumSize(new java.awt.Dimension(124, 35));
        key.setPreferredSize(new java.awt.Dimension(289, 50));
        key.setSelectedTextColor(new java.awt.Color(102, 102, 102));
        key.setSelectionColor(new java.awt.Color(175, 238, 238));
        key.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                keyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                keyFocusLost(evt);
            }
        });
        key.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keyKeyReleased(evt);
            }
        });
        leftPanel.add(key, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));

        exitBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/exitMouseHoverONcolored.png"))); // NOI18N
        exitBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exitBtn.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/exitMouseHoverOFF.png"))); // NOI18N
        exitBtn.setEnabled(false);
        exitBtn.setMaximumSize(new java.awt.Dimension(20, 20));
        exitBtn.setPreferredSize(new java.awt.Dimension(16, 20));
        exitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitBtnMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exitBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                exitBtnMouseReleased(evt);
            }
        });
        leftPanel.add(exitBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 20, 20));

        minimizeBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimizeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/minimizeMouseHoverONcolored.png"))); // NOI18N
        minimizeBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        minimizeBtn.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/minimizeMouseHoverOFF.png"))); // NOI18N
        minimizeBtn.setEnabled(false);
        minimizeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizeBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizeBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizeBtnMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                minimizeBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                minimizeBtnMouseReleased(evt);
            }
        });
        leftPanel.add(minimizeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 30, 20));

        processBtnPanel.setBackground(new java.awt.Color(216, 216, 216));
        processBtnPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        processBtnPanel.setMaximumSize(new java.awt.Dimension(255, 42));
        processBtnPanel.setMinimumSize(new java.awt.Dimension(255, 42));
        processBtnPanel.setPreferredSize(new java.awt.Dimension(255, 42));

        processBtn.setBackground(new java.awt.Color(133, 221, 221));
        processBtn.setFont(new java.awt.Font("Iosevka SS07 Extrabold", 3, 18)); // NOI18N
        processBtn.setForeground(new java.awt.Color(130, 130, 130));
        processBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/yes.png"))); // NOI18N
        processBtn.setText("ENCRYPT! ^-^");
        processBtn.setToolTipText("<html>\n  </head>\n    <style>\n      p {color: #993366; font-family: sans-serif;}\n    </style>\n  </head>\n\n  <body>\n    <p>enter some text and add a key to enable the button</p>\n  </body>\n</html>");
        processBtn.setBorderPainted(false);
        processBtn.setContentAreaFilled(false);
        processBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        processBtn.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/no.png"))); // NOI18N
        processBtn.setEnabled(false);
        processBtn.setFocusPainted(false);
        processBtn.setIconTextGap(10);
        processBtn.setMaximumSize(new java.awt.Dimension(260, 42));
        processBtn.setMinimumSize(new java.awt.Dimension(260, 42));
        processBtn.setPreferredSize(new java.awt.Dimension(260, 42));
        processBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                processBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                processBtnMouseReleased(evt);
            }
        });
        processBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout processBtnPanelLayout = new javax.swing.GroupLayout(processBtnPanel);
        processBtnPanel.setLayout(processBtnPanelLayout);
        processBtnPanelLayout.setHorizontalGroup(
            processBtnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(processBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 254, Short.MAX_VALUE)
        );
        processBtnPanelLayout.setVerticalGroup(
            processBtnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(processBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, Short.MAX_VALUE)
        );

        leftPanel.add(processBtnPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 255, 42));

        togglePanel.setBackground(new java.awt.Color(133, 221, 221));
        togglePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 255, 255), new java.awt.Color(204, 255, 255), new java.awt.Color(0, 153, 153), new java.awt.Color(0, 153, 153)));
        togglePanel.setMaximumSize(new java.awt.Dimension(30, 42));
        togglePanel.setMinimumSize(new java.awt.Dimension(30, 42));
        togglePanel.setPreferredSize(new java.awt.Dimension(30, 42));

        decryptionToggle.setBackground(new java.awt.Color(133, 221, 221));
        decryptionToggle.setFont(new java.awt.Font("Iosevka Fixed SS12", 3, 12)); // NOI18N
        decryptionToggle.setForeground(new java.awt.Color(68, 68, 68));
        decryptionToggle.setToolTipText("<html>\n  </head>\n    <style>\n      p {color: #993366; font-family: sans-serif;}\n    </style>\n  </head>\n\n  <body>\n    <p>switch ON to enter decryption mode</p>\n  </body>\n</html>");
        decryptionToggle.setAlignmentX(0.5F);
        decryptionToggle.setBorderPainted(false);
        decryptionToggle.setContentAreaFilled(false);
        decryptionToggle.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        decryptionToggle.setFocusPainted(false);
        decryptionToggle.setMaximumSize(new java.awt.Dimension(30, 42));
        decryptionToggle.setMinimumSize(new java.awt.Dimension(30, 42));
        decryptionToggle.setPreferredSize(new java.awt.Dimension(30, 42));
        decryptionToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decryptionToggleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout togglePanelLayout = new javax.swing.GroupLayout(togglePanel);
        togglePanel.setLayout(togglePanelLayout);
        togglePanelLayout.setHorizontalGroup(
            togglePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
            .addGroup(togglePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(togglePanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(decryptionToggle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        togglePanelLayout.setVerticalGroup(
            togglePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
            .addGroup(togglePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(togglePanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(decryptionToggle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        leftPanel.add(togglePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 400, 30, 42));
        leftPanel.add(charCounterSep, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 310, 40, 10));

        charCounterLabel.setBackground(new java.awt.Color(0, 0, 0));
        charCounterLabel.setFont(new java.awt.Font("Major Mono Display", 0, 13)); // NOI18N
        charCounterLabel.setForeground(new java.awt.Color(216, 216, 216));
        charCounterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        charCounterLabel.setLabelFor(key);
        charCounterLabel.setText("20");
        charCounterLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        charCounterLabel.setMaximumSize(new java.awt.Dimension(290, 35));
        charCounterLabel.setMinimumSize(new java.awt.Dimension(290, 35));
        charCounterLabel.setPreferredSize(new java.awt.Dimension(290, 35));
        leftPanel.add(charCounterLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 280, 40, 30));

        mainPanel.add(leftPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 370, 480));

        rightPanel.setBackground(new java.awt.Color(255, 153, 102));
        rightPanel.setAlignmentX(0.0F);
        rightPanel.setMaximumSize(new java.awt.Dimension(310, 520));
        rightPanel.setMinimumSize(new java.awt.Dimension(310, 520));
        rightPanel.setPreferredSize(new java.awt.Dimension(310, 520));
        rightPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        titleLabel.setBackground(new java.awt.Color(91, 91, 91));
        titleLabel.setFont(new java.awt.Font("Victor Mono SemiBold", 1, 28)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(91, 91, 91));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setLabelFor(this);
        titleLabel.setText("Vigenère Cipher");
        titleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rightPanel.add(titleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 80, 300, -1));

        versionLabel.setBackground(new java.awt.Color(51, 51, 51));
        versionLabel.setFont(new java.awt.Font("Victor Mono Light", 2, 11)); // NOI18N
        versionLabel.setForeground(new java.awt.Color(51, 51, 51));
        versionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        versionLabel.setLabelFor(this);
        versionLabel.setText("v1.0");
        versionLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rightPanel.add(versionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, -1, -1));

        titleBottomSep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleBottomSep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/titleBottomSep.png"))); // NOI18N
        titleBottomSep.setLabelFor(titleLabel);
        titleBottomSep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rightPanel.add(titleBottomSep, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 310, 30));

        titleTopSep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleTopSep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/titleTopSep.png"))); // NOI18N
        titleTopSep.setLabelFor(titleLabel);
        titleTopSep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rightPanel.add(titleTopSep, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 310, -1));

        titleMiddleSep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleMiddleSep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/titleMiddleSep.png"))); // NOI18N
        titleMiddleSep.setLabelFor(titleLabel);
        titleMiddleSep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rightPanel.add(titleMiddleSep, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 130, 305, -1));

        lockIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lockIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/locked.png"))); // NOI18N
        lockIcon.setLabelFor(processBtn);
        lockIcon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rightPanel.add(lockIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 170, 40, 40));

        resultTextPanel.setMaximumSize(new java.awt.Dimension(240, 210));
        resultTextPanel.setMinimumSize(new java.awt.Dimension(240, 210));
        resultTextPanel.setPreferredSize(new java.awt.Dimension(240, 210));

        resultText.setEditable(false);
        resultText.setBackground(new java.awt.Color(68, 68, 68));
        resultText.setColumns(20);
        resultText.setFont(new java.awt.Font("Source Code Pro", 0, 14)); // NOI18N
        resultText.setForeground(new java.awt.Color(204, 204, 204));
        resultText.setLineWrap(true);
        resultText.setWrapStyleWord(true);
        resultText.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 172, 153), new java.awt.Color(255, 172, 153), new java.awt.Color(255, 149, 96), new java.awt.Color(255, 149, 96)));
        resultText.setCaretColor(new java.awt.Color(255, 127, 63));
        resultText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        resultText.setMaximumSize(new java.awt.Dimension(240, 210));
        resultText.setMinimumSize(new java.awt.Dimension(240, 210));
        resultText.setSelectedTextColor(new java.awt.Color(102, 102, 102));
        resultText.setSelectionColor(new java.awt.Color(255, 167, 147));
        /*waitingDots.start();*/
        WaitingDots wd = new WaitingDots();
        Thread t = new Thread(wd, "waiting dots thread");
        t.start();
        resultText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                resultTextFocusGained(evt);
            }
        });
        resultTextPanel.setViewportView(resultText);

        rightPanel.add(resultTextPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 220, 240, 210));

        twitterLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        twitterLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/twitterLogo.png"))); // NOI18N
        twitterLogo.setLabelFor(this);
        twitterLogo.setToolTipText("<html>   </head>     <style>       p {color: #993366; font-family: sans-serif;}     </style>   </head>    <body>     <p>View my profile on Twitter</p> </body> </html>");
        twitterLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        twitterLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        twitterLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                twitterLogoMouseClicked(evt);
            }
        });
        rightPanel.add(twitterLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 491, -1, 20));

        githubLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        githubLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/githubLogo.png"))); // NOI18N
        githubLogo.setLabelFor(this);
        githubLogo.setToolTipText("<html>   </head>     <style>       p {color: #993366; font-family: sans-serif;}     </style>   </head>    <body>     <p>Visit my GitHub profile for more projects</p> </body> </html>");
        githubLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        githubLogo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        githubLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                githubLogoMouseClicked(evt);
            }
        });
        rightPanel.add(githubLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 488, 20, -1));

        websiteLabel.setBackground(new java.awt.Color(51, 51, 51));
        websiteLabel.setFont(new java.awt.Font("Victor Mono Light", 2, 10)); // NOI18N
        websiteLabel.setForeground(new java.awt.Color(51, 51, 51));
        websiteLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        websiteLabel.setText("view website");
        websiteLabel.setToolTipText("<html>   </head>     <style>       p {color: #993366; font-family: sans-serif;}     </style>   </head>    <body>     <p>Opens the project's website in the default browser</p> </body> </html>");
        websiteLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        websiteLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        websiteLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                websiteLabelMouseClicked(evt);
            }
        });
        rightPanel.add(websiteLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 490, 100, 20));

        copyrightLabel.setBackground(new java.awt.Color(51, 51, 51));
        copyrightLabel.setFont(new java.awt.Font("Victor Mono Light", 0, 10)); // NOI18N
        copyrightLabel.setForeground(new java.awt.Color(51, 51, 51));
        copyrightLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        copyrightLabel.setLabelFor(this);
        copyrightLabel.setText("© 2020 Elias Rahma");
        copyrightLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rightPanel.add(copyrightLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 130, -1));

        saveBtnPanel.setBackground(new java.awt.Color(81, 81, 81));
        saveBtnPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 155, 132), new java.awt.Color(255, 155, 132), new java.awt.Color(255, 117, 51), new java.awt.Color(255, 117, 51)));

        saveBtn.setBackground(new java.awt.Color(68, 68, 68));
        saveBtn.setFont(new java.awt.Font("Iosevka SS07 Extrabold", 0, 10)); // NOI18N
        saveBtn.setForeground(new java.awt.Color(204, 204, 204));
        saveBtn.setText("save");
        saveBtn.setBorderPainted(false);
        saveBtn.setContentAreaFilled(false);
        saveBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saveBtn.setFocusPainted(false);
        saveBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveBtnMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                saveBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                saveBtnMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout saveBtnPanelLayout = new javax.swing.GroupLayout(saveBtnPanel);
        saveBtnPanel.setLayout(saveBtnPanelLayout);
        saveBtnPanelLayout.setHorizontalGroup(
            saveBtnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
        );
        saveBtnPanelLayout.setVerticalGroup(
            saveBtnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        rightPanel.add(saveBtnPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(186, 175, 87, 30));

        progressBar.setBackground(new java.awt.Color(68, 68, 68));
        progressBar.setFont(new java.awt.Font("Iosevka SS07 Extrabold", 2, 12)); // NOI18N
        progressBar.setForeground(new java.awt.Color(153, 0, 204));
        progressBar.setBorder(null);
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        rightPanel.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 177, 90, 27));

        timeTakenLabel.setBackground(new java.awt.Color(51, 51, 51));
        timeTakenLabel.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        timeTakenLabel.setForeground(new java.awt.Color(51, 51, 51));
        timeTakenLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeTakenLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rightPanel.add(timeTakenLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 490, 160, 20));

        mainPanel.add(rightPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, 310, 520));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // ===========================
    // ||                       ||
    // ||  GENERATED FUNCTIONS  ||
    // ||                       ||
    // ===========================
    
    private void processBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processBtnActionPerformed
        progressBar.setVisible(true);
        percentageTotal = 0; tempPerTotal = 0; perTempCalc = 0;
        progressBar.setValue((int) percentageTotal);

        Thread cryptionProcess = new Thread(() -> {
            // -------------------
            // | initialisations |
            // -------------------
            
            final long startTime = System.currentTimeMillis(); // Stopwatch start time
            LOGGER.log(Level.INFO, "Cryption process started...");
            
            String arrangement = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 1234567890\n~`!@#$%^&*()_-=+{}[]:;'\"\\|,<.>/?";
            char[] arrangedChars = arrangement.toCharArray();
            //System.out.println(arrangement.length());
            //System.out.println(arrangedChars.length);
            //System.out.println("array: " + Arrays.toString(arrangedChars));
            
            // a. finding the index of each character entered
            // -----------------------------------------------
            char[] messageChars = message.getText().toCharArray();
            int[] messageCharsIndices = findIndices(arrangedChars, messageChars); // takes time when seaching for characters; might wanna enhance the code later [with printing, otherwise actually is extremely quick]
            //System.out.println(Arrays.toString(messageChars));
            //System.out.println(Arrays.toString(messageCharsIndices));
            
            char[] keyChars = key.getText().toCharArray();
            int[] keyCharsIndices = findIndices(arrangedChars, keyChars); // takes time when seaching for characters; might wanna enhance the code later [with printing, otherwise actually is extremely quick]
            //System.out.println(Arrays.toString(keyChars));
            //System.out.println(Arrays.toString(keyCharsIndices));
            // -----------------------------------------------
            
            // b. merging the MESSAGE's indices with the KEY's indices
            // -----------------------------------------------
            int[][] mergedIndices = mergeIndices(messageCharsIndices, keyCharsIndices); // takes less time than finding the characters [with printing, otherwise actually is extremely quick]
            //System.out.format("arrangement length = %d\n%s\n", arrangedChars.length, Arrays.deepToString(mergedIndices));
            // -----------------------------------------------
            
            // c. Encrypting & Decrypting
            // -----------------------------------------------
            String processResult;
            if (decryptionToggle.isSelected() == false) {
                // --------------
                // | ENCRYPTION |
                // --------------
                processResult = vigenereCipher(arrangedChars, mergedIndices, true); // takes the longest time with/wout printing [update: now its SUPER FAST! time-wastage dropped by 99%]
                
            } else {
                // --------------
                // | DECRYPTION |
                // --------------
                processResult = vigenereCipher(arrangedChars, mergedIndices, false); // takes the longest time with/wout printing  [update: now its SUPER FAST! time-wastage dropped by 99%]
            }
            // -----------------------------------------------
            
            resultText.setText(processResult);
            saveBtnPanel.setVisible(true);
            
            final long finishTime = System.currentTimeMillis(); // Stopwatch finish time
            //System.out.println("\ntime taken to process everything     : " + ((double) (finishTime - startTime) / 1000.0) + " seconds\n");
            timeTakenLabel.setVisible(true);
            timeTakenLabel.setText("time taken: " + ((double) (finishTime - startTime) / 1000.0) + " seconds");
            
            LOGGER.log(Level.INFO, "Cryption process finished...");
        });
        
        cryptionProcess.start();
    }//GEN-LAST:event_processBtnActionPerformed

    private void decryptionToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decryptionToggleActionPerformed
        if (decryptionToggle.isSelected() == true) {
            // --------------------------------
            // | When the toggle is turned ON |
            // --------------------------------
            
            processBtn.setText("DECRYPT! ^-^");
            //decryptionToggle.setBackground(new Color(255, 163, 255));
            
            toggleTheme(true);
            
            message.setToolTipText("<html>\n"
                    + "  </head>\n"
                    + "    <style>\n"
                    + "      p {color: #993366; font-family: sans-serif;}\n"
                    + "    </style>\n"
                    + "  </head>\n"
                    + "\n"
                    + "  <body>\n"
                    + "    <p>copy paste the encrypted text to decrypt using the vigenere cipher hehe ^-^</p>\n"
                    + "  </body>\n"
                    + "</html>");
            key.setToolTipText("<html>\n"
                    + "  </head>\n"
                    + "    <style>\n"
                    + "      p {color: #993366; font-family: sans-serif;}\n"
                    + "    </style>\n"
                    + "  </head>\n"
                    + "\n"
                    + "  <body>\n"
                    + "    <p>enter the passcode you used during the encryption process</p>\n"
                    + "    <p>i hope you remember it though!</p>\n"
                    + "  </body>\n"
                    + "</html>");
            decryptionToggle.setToolTipText("<html>\n"
                    + "  </head>\n"
                    + "    <style>\n"
                    + "      p {color: #993366; font-family: sans-serif;}\n"
                    + "    </style>\n"
                    + "  </head>\n"
                    + "\n"
                    + "  <body>\n"
                    + "    <p>switch OFF to enter encryption mode</p>\n"
                    + "  </body>\n"
                    + "</html>");
            
        } else {
            // ---------------------------------
            // | When the toggle is turned OFF |
            // ---------------------------------
            
            processBtn.setText("ENCRYPT! ^-^");
            //decryptionToggle.setBackground(new Color(133, 221, 221));
            
            toggleTheme(false);
            
            message.setToolTipText("<html>\n"
                    + "  </head>\n"
                    + "    <style>\n"
                    + "      p {color: #993366; font-family: sans-serif;}\n"
                    + "    </style>\n"
                    + "  </head>\n"
                    + "\n"
                    + "  <body>\n"
                    + "    <p>enter your secret message to encrypt using the vigenere cipher hehe ^-^</p>\n"
                    + "  </body>\n"
                    + "</html>");
            key.setToolTipText("<html>\n"
                    + "  </head>\n"
                    + "    <style>\n"
                    + "      p {color: #993366; font-family: sans-serif;}\n"
                    + "    </style>\n"
                    + "  </head>\n"
                    + "\n"
                    + "  <body>\n"
                    + "    <p>enter a passcode so you can secure it</p>\n"
                    + "    <p>make sure to remember it though!</p>\n"
                    + "  </body>\n"
                    + "</html>");
            decryptionToggle.setToolTipText("<html>\n"
                    + "  </head>\n"
                    + "    <style>\n"
                    + "      p {color: #993366; font-family: sans-serif;}\n"
                    + "    </style>\n"
                    + "  </head>\n"
                    + "\n"
                    + "  <body>\n"
                    + "    <p>switch ON to enter decryption mode</p>\n"
                    + "  </body>\n"
                    + "</html>");
            
        }
    }//GEN-LAST:event_decryptionToggleActionPerformed

    private void messageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageKeyReleased
        changeProcessBtnStyle();
        /*if (message.getText().length() > 0) {
            processFinished = true;
        }*/
    }//GEN-LAST:event_messageKeyReleased

    private void exitBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitBtnMouseEntered
        exitBtn.setEnabled(true);
    }//GEN-LAST:event_exitBtnMouseEntered

    private void exitBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitBtnMouseExited
        exitBtn.setEnabled(false);
    }//GEN-LAST:event_exitBtnMouseExited

    private void exitBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitBtnMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) { System.exit(0); }
    }//GEN-LAST:event_exitBtnMouseClicked

    private void minimizeBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeBtnMouseEntered
        minimizeBtn.setEnabled(true);
    }//GEN-LAST:event_minimizeBtnMouseEntered

    private void minimizeBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeBtnMouseExited
        minimizeBtn.setEnabled(false);
    }//GEN-LAST:event_minimizeBtnMouseExited

    private void minimizeBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeBtnMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) { this.setState(JFrame.ICONIFIED); }
    }//GEN-LAST:event_minimizeBtnMouseClicked

    private void mainPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMouseDragged
        /*System.out.format("frame location (x): %d\n"
                + "drag stop location (x): %d\n"
                + "frame location (y): %d\n"
                + "drag stop location (y): %d\n", this.getLocation().x, evt.getX(), this.getLocation().y, evt.getY());*/
        this.setLocation(this.getLocation().x + evt.getX() - tempCorX,
                this.getLocation().y + evt.getY() - tempCorY);
    }//GEN-LAST:event_mainPanelMouseDragged

    static int tempCorX, tempCorY;
    private void mainPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainPanelMousePressed
        tempCorX = evt.getX();
        tempCorY = evt.getY();
        /*System.out.format("temp X: %d\n"
                + "temp Y: %d\n", tempCorX, tempCorY);*/
    }//GEN-LAST:event_mainPanelMousePressed

    private void processBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_processBtnMouseReleased
        if (evt.getButton() == MouseEvent.BUTTON1 && message.getText().length() > 0 && key.getText().length() > 2 && key.getText().length() <= 20) {
            if (decryptionToggle.isSelected() == true) {
                processBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(255,219,224), new Color(255,219,224), new Color(255,122,142), new Color(255,122,142)));
            } else {
                processBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(204,255,255), new Color(204,255,255), new Color(0,153,153), new Color(0,153,153)));
            }
            processBtn.setMargin(new Insets(2, 14, 2, 14));
        }
    }//GEN-LAST:event_processBtnMouseReleased

    private void processBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_processBtnMousePressed
        if (evt.getButton() == MouseEvent.BUTTON1 && message.getText().length() > 0 && key.getText().length() > 2 && key.getText().length() <= 20) {
            if (decryptionToggle.isSelected() == true) {
                processBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, new Color(255,219,224), new Color(255,219,224), new Color(255,122,142), new Color(255,122,142)));
            } else {
                processBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, new Color(204,255,255), new Color(204,255,255), new Color(0,153,153), new Color(0,153,153)));
            }
            processBtn.setMargin(new Insets(6, 18, 2, 14));
        }
    }//GEN-LAST:event_processBtnMousePressed

    static int maxKeyChars = 20;
    private void keyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyKeyReleased
        changeProcessBtnStyle();
        
        if (maxKeyChars - key.getText().length() < 0) {
            charCounterLabel.setForeground(new Color(255,102,102)); // red
        } else if (key.getText().length() < 3) {
            charCounterLabel.setForeground(new Color(216,216,216)); // gray
        } else {
            charCounterLabel.setForeground(new Color(102,255,102)); // green
        }
        
        if (maxKeyChars - key.getText().length() == -99) {
            charCounterLabel.setText("-99");
        } else if (maxKeyChars - key.getText().length() < -99) {
            charCounterLabel.setText("-99+");
        } else {
            charCounterLabel.setText(maxKeyChars - key.getText().length() + "");
        }
    }//GEN-LAST:event_keyKeyReleased

    private void keyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_keyFocusGained
        charCounterLabel.setVisible(true);
        charCounterSep.setVisible(true);
        if (message.getText().length() > 0) {
            LOGGER.log(Level.INFO, "Requesting buster call...");
            processFinished = true;
            LOGGER.log(Level.INFO, "Buster Call Initiated!!! Everyone evacuate immediately!!");
        }
    }//GEN-LAST:event_keyFocusGained

    private void messageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_TAB) {
            key.requestFocus();
        }
    }//GEN-LAST:event_messageKeyPressed

    private void messageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_messageFocusLost
        message.setText(message.getText().replaceAll("\t", ""));
    }//GEN-LAST:event_messageFocusLost

    private void keyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_keyFocusLost
        if (maxKeyChars - key.getText().length() >= 0) {
            charCounterLabel.setVisible(false);
            charCounterSep.setVisible(false);
        }
    }//GEN-LAST:event_keyFocusLost

    private void resultTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resultTextFocusGained
        resultText.selectAll();
    }//GEN-LAST:event_resultTextFocusGained

    private void twitterLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_twitterLogoMouseClicked
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://twitter.com/TeryakiiSauce"));
            } catch (URISyntaxException ex) {
                LOGGER.log(Level.SEVERE, "URL format is invalid, more info:\n", ex);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Something went wrong, more info:\n", ex);
            }
        }
    }//GEN-LAST:event_twitterLogoMouseClicked

    private void githubLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_githubLogoMouseClicked
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/TeryakiiSauce"));
            } catch (URISyntaxException ex) {
                LOGGER.log(Level.SEVERE, "URL format is invalid, more info:\n", ex);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Something went wrong, more info:\n", ex);
            }
        }
    }//GEN-LAST:event_githubLogoMouseClicked

    private void saveBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveBtnMousePressed
        if (evt.getButton() == MouseEvent.BUTTON1) {
            saveBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, new Color(255,155,132), new Color(255,155,132), new Color(255,117,51), new Color(255,117,51)));
        }
        saveBtn.setMargin(new Insets(4, 15, 2, 14));
    }//GEN-LAST:event_saveBtnMousePressed

    private void saveBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveBtnMouseReleased
        if (evt.getButton() == MouseEvent.BUTTON1) {
            saveBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(255,155,132), new Color(255,155,132), new Color(255,117,51), new Color(255,117,51)));
        }
        saveBtn.setMargin(new Insets(2, 14, 2, 14));
    }//GEN-LAST:event_saveBtnMouseReleased

    private void saveBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveBtnMouseClicked
        //System.out.println("file chooser is to be shown");
        JFileChooser fileChooserDialog = new JFileChooser();
        fileChooserDialog.setDialogTitle("^-^ choose a destination to save the file (also the .txt extension will be added automatically so dw!)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("text files", "txt");
        fileChooserDialog.setFileFilter(filter);
        
        if (fileChooserDialog.showSaveDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooserDialog.getSelectedFile();
            if (!fileToSave.getName().matches(".*\\.txt$")) { // finds if the file name entered in the dialog matches the regex expression (to find .txt at the end of the file name)
                LOGGER.log(Level.WARNING, "File name entered does NOT contain .txt at the end");
                String updatedFileName;
                try {
                    updatedFileName = fileToSave.getCanonicalFile()+".txt";
                    fileToSave = new File(updatedFileName);
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "An error occured, more info:\n", ex);
                }
            }
            
            fileToSave.setWritable(true);
            
            BufferedWriter writer = null;
            try {
                // a. Stuff to put inside the file
                // -----------------------------------------------
                StringBuilder result = new StringBuilder();
                java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat("MMM d yyy @ HH:mm:ss");
                
                result.append("================================================\n");
                result.append("| DATE GENERATED : ").append(dateFormatter.format(new java.util.Date())).append("\n");
                result.append("| CREATED BY     : Elias Rahma (@TeryakiiSauce)\n");
                result.append("| FILE DIRECTORY : ").append(fileToSave.getCanonicalPath()).append("\n");
                result.append("================================================\n\n");
                
                result.append("A. KEY: ").append(key.getText()).append("\n");
                result.append("================================================\n\n");
                
                if (!decryptionToggle.isSelected()) {
                    // ENCRYPTION MODE
                    result.append("B. ENCRYPTED TEXT (when copying make sure to\n");
                    result.append("select everything right after the seperator\n");
                    result.append("line, even if the first line is empty!)\n");
                } else {
                    // DECRYPTION MODE
                    result.append("B. DECRYPTED TEXT\n");
                }
                
                result.append("================================================\n").append(resultText.getText());
                
                //String result = resultText.getText() + "\n=============================\nKEY: " + key.getText();
                //System.out.println(result.toString());
                
                // b. Checks if the file exists
                // -----------------------------------------------
                if (!fileToSave.exists()) {
                    FileWriter fw = new FileWriter(fileToSave);
                    writer = new BufferedWriter(fw);
                    writer.write(result.toString());
                } else {
                    LOGGER.log(Level.WARNING, "File already exists!");
                    int choice = JOptionPane.showConfirmDialog(mainPanel, "file name entered already exists in the current directory...\n"
                            + "wanna replace it?", "file already exists", JOptionPane.INFORMATION_MESSAGE);
                    if (choice == JOptionPane.YES_OPTION) {
                        LOGGER.log(Level.INFO, "User chose to replace the file");
                        FileWriter fw = new FileWriter(fileToSave);
                        writer = new BufferedWriter(fw);
                        writer.write(result.toString());
                    }
                }
                
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "An error occured, more info:\n", e);
                
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                        fileToSave.setReadable(true, true); // only readable by owner
                        fileToSave.setReadOnly();
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().edit(fileToSave);
                        }
                    } catch (IOException ex) {
                        LOGGER.log(Level.SEVERE, "An error occured, more info:\n", ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_saveBtnMouseClicked

    private void exitBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitBtnMousePressed
        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/exitMousePressed.png")));
    }//GEN-LAST:event_exitBtnMousePressed

    private void exitBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitBtnMouseReleased
        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/exitMouseHoverONcolored.png")));
    }//GEN-LAST:event_exitBtnMouseReleased

    private void minimizeBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeBtnMousePressed
        minimizeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/minimizeMousePressed.png")));
    }//GEN-LAST:event_minimizeBtnMousePressed

    private void minimizeBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeBtnMouseReleased
        minimizeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/minimizeMouseHoverONcolored.png")));
    }//GEN-LAST:event_minimizeBtnMouseReleased

    private void websiteLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_websiteLabelMouseClicked
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://teryakiisauce.github.io/Vigenere-Cipher/"));
            } catch (URISyntaxException ex) {
                LOGGER.log(Level.SEVERE, "URL format is invalid, more info:\n", ex);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Something went wrong, more info:\n", ex);
            }
        }
    }//GEN-LAST:event_websiteLabelMouseClicked

    // ==========================
    // ||                      ||
    // ||   CUSTOM FUNCTIONS   ||
    // ||                      ||
    // ==========================
    
    /**
     * changes state of the process button and some styles as well
     * Eg: the button becomes grayed out when no data is entered (bc it would be useless otherwise)
     * 
     * PS. more can be found in the [processBtnPanel] mouse events methods
     */
    public void changeProcessBtnStyle() {
        if (message.getText().length() > 0 && key.getText().length() > 2 && key.getText().length() <= 20) {
            // -------------------------------------
            // | WHEN BOTH TEXT BOXES CONTAIN DATA |
            // -------------------------------------
            
            // a. process button turns ON
            // -----------------------------------------------
            processBtn.setEnabled(true);
            
            // b. tool tip text changes
            // -----------------------------------------------
            processBtn.setToolTipText("<html>\n"
                    + "  </head>\n"
                    + "    <style>\n"
                    + "      p {color: #993366; font-family: sans-serif;}\n"
                    + "    </style>\n"
                    + "  </head>\n"
                    + "\n"
                    + "  <body>\n"
                    + "    <p>let the process begin yeehaw!</p>\n"
                    + "  </body>\n"
                    + "</html>");
            
            // c. background & border changes to either blue or pink, depending on the current theme
            // -----------------------------------------------
            if (decryptionToggle.isSelected() == true) {
                processBtnPanel.setBackground(new Color(255, 182, 193));
                processBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(255,219,224), new Color(255,219,224), new Color(255,122,142), new Color(255,122,142)));
            } else {
                processBtnPanel.setBackground(new Color(133,221,221));
                processBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(204,255,255), new Color(204,255,255), new Color(0,153,153), new Color(0,153,153)));
            }
            
        } else {
            // ----------------------------------
            // | WHEN BOTH TEXT BOXES ARE EMPTY |
            // ----------------------------------
            
            // a. the process button turns OFF
            // -----------------------------------------------
            processBtn.setEnabled(false);
            
            // b. tool tip text changes
            // -----------------------------------------------
            processBtn.setToolTipText("<html>\n"
                    + "  </head>\n"
                    + "    <style>\n"
                    + "      p {color: #993366; font-family: sans-serif;}\n"
                    + "    </style>\n"
                    + "  </head>\n"
                    + "\n"
                    + "  <body>\n"
                    + "    <p>enter some text and add a key to enable the button</p>\n"
                    + "  </body>\n"
                    + "</html>");
            
            // c. border changes to disabled colors
            // -----------------------------------------------
            processBtnPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            
            // d. background changes to gray
            // -----------------------------------------------
            processBtnPanel.setBackground(new Color(216, 216, 216));
        }
    }
    
    /**
     * use 1D array! returns the original character of each index given in an array using the passed
     * integers of the original arrangement as reference
     * @param arrangementArr the arrangement array. eg: A-Z, a-z, 0-9
     * @param intArr the array that includes the indices to be searched
     * @return the characters of each index as an appended string
     */
    public String findChars (char[] arrangementArr, int[] intArr) {
        //final long startTime = System.currentTimeMillis(); // Stopwatch start time
        
        //char[] chars = new char[intArr.length];
        StringBuilder sb = new StringBuilder();
        
        /*if (intArr.length <= 100) {
            sb = new StringBuilder(100);
        } else if (intArr.length > 100 && intArr.length <= 1000) {
            sb = new StringBuilder(1000);
        } */
        
        for (int i = 0; i < intArr.length; i++) {
            //chars[i] = arrangementArr[intArr[i]];
            sb.append(arrangementArr[intArr[i]]);
            //System.out.println("index [" + intArr[i] + "] has the character [" + chars[i] + "]");
            
            /**
             * Setting the progress bar value
             */
            perTempCalc = ((double) i / (double) intArr.length * (double) progressBar.getMaximum());
            perTempCalc /= 4;
        }
        
        percentageTotal *= 4;
        progressBar.setValue((int) percentageTotal);
        
        //final long finishTime = System.currentTimeMillis(); // Stopwatch finish time
        //System.out.println("time taken for [     findChars()    ]: " + ((double) (finishTime - startTime) / 1000.0) + " seconds");
        
        //System.out.println(sb.toString());
        return sb.toString();
    }
    
    double percentageTotal = 0, tempPerTotal = 0, perTempCalc = 0;
    /**
     * use 1D array! returns the index of each character using the given arrangement array as reference
     * @param arrangementArr the arrangement array. eg: A-Z, a-z, 0-9
     * @param chrArr the array that includes the characters to be searched
     * @return the index of each character in an array of type int[]
     */
    public int[] findIndices (char[] arrangementArr, char[] chrArr) {
        //final long startTime = System.currentTimeMillis(); // Stopwatch start time
        
        perTempCalc = 0;
        int[] charsIndices = new int[chrArr.length];
        for (int i = 0; i < chrArr.length; i++) {
            for (int j = 0; j < arrangementArr.length; j++) {
                if (chrArr[i] == arrangementArr[j]) {
                    //System.out.println(i + ". char [" + chrArr[i] + "]'s index is = " + j);
                    charsIndices[i] = j;
                    
                    /**
                     * Setting the progress bar value
                     */
                    //System.out.println("chrArr length: "+ chrArr.length);
                    //System.out.println("current per: " + perTempCalc);
                    perTempCalc = ((double) i / (double) chrArr.length * (double) progressBar.getMaximum());
                    perTempCalc /= 4; // 4 is chosen because there are four parts where the percentage will be calculated [1. This method, 2. mergeIndices, 3. vigenereCipher & 4. findChars]
                    //System.out.println("total per: " + perTempCalc);
                    
                    progressBar.setValue((int) perTempCalc + (int) tempPerTotal);
                    perTempCalc = 50; // the 50 is chosen bc this method is called twice and so idk how to explain it... ill regret it later smh ~ the variable will updated every loop but dw it will resign a new value each loop without displaying this value
                }
            }
        }
        
        tempPerTotal += perTempCalc;                    // ===================================================================
        percentageTotal = tempPerTotal / 4;             // | THESE ARE TO CALC THE % OF THE FIRST SECTION ONLY (THIS METHOD) |
        progressBar.setValue((int) percentageTotal);    // ===================================================================
        
        //final long finishTime = System.currentTimeMillis(); // Stopwatch finish time
        //System.out.println("time taken for [    findIndices()   ]: " + ((double) (finishTime - startTime) / 1000.0) + " seconds");
        
        return charsIndices;
    }
    
    /**
     * merges the indices passed as parameters together
     * @param msgIndices the MESSAGE's indices array
     * @param keyIndices the KEY's indices array
     * @return a 2D array where the arrays passed are merged together, you can check the example below
     */
    public int[][] mergeIndices(int[] msgIndices, int[] keyIndices) {
        //final long startTime = System.currentTimeMillis(); // Stopwatch start time
        
        /*
        Array style example:
        --------------------------
        | 12 | 15 | 37 | 14 | 27 | -> MESSAGE's indices
        | 4  | 7  | 5  | 4  | 7  | -> KEY's indices; repeats until MESSAGE is finished
        | 16 | 22 | 42 | 18 | 34 | -> Addition/ Subtraction of both arrays (for later steps)
        --------------------------
        */
        
        perTempCalc = 0;
        int[][] mergedArrays = new int[msgIndices.length][2]; // a new array with the same size of the MESSAGE's characters
        for (int i = 0, tempCounter = 0; i < mergedArrays.length; i++) {
            mergedArrays[i][0] = msgIndices[i]; // first row
            if (tempCounter >= keyIndices.length) { tempCounter = 0; }
            mergedArrays[i][1] = keyIndices[tempCounter]; // second row
            tempCounter++;
            
            /**
             * assigning values to calculate the progress bar values
             */
            //System.out.println("per total: " + percentageTotal);
            perTempCalc = ((double) i / (double) mergedArrays.length * (double) progressBar.getMaximum());
            perTempCalc /= 4;
            //System.out.println("temp per: " + perTempCalc);
            progressBar.setValue((int) perTempCalc + (int) percentageTotal);
        }
        
        percentageTotal *= 2;
        progressBar.setValue((int) percentageTotal);
        
        //final long finishTime = System.currentTimeMillis(); // Stopwatch finish time
        //System.out.println("time taken for [   mergeIndices()   ]: " + ((double) (finishTime - startTime) / 1000.0) + " seconds");
        
        return mergedArrays;
    }
    
    /**
     * changes the theme of the app when the decryption toggle is switched between ON or OFF.
     * PINK Theme = [true]
     * BLUE Theme = [false]
     * @param toggle [true] = toggle turned ON; [false] = toggle turned OFF
     */
    public void toggleTheme(boolean toggle) {
        if (toggle == true) {
            // -------------------------------------
            // | PINK THEME; when toggle turned ON |
            // -------------------------------------
            
            key.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(255,219,224), new Color(255,219,224), new Color(255,122,142), new Color(255,122,142)));
            key.setSelectionColor(new Color(255,204,255));
            leftPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 192, 203), 10));
            message.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(255,219,224), new Color(255,219,224), new Color(255,122,142), new Color(255,122,142)));
            message.setSelectionColor(new Color(255,204,255));
            if (message.getText().length() > 0 && key.getText().length() > 0 && processBtn.isEnabled()) {
                processBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(255,219,224), new Color(255,219,224), new Color(255,122,142), new Color(255,122,142)));
                processBtnPanel.setBackground(new Color(255, 182, 193));
            }
            togglePanel.setBackground(new Color(255, 182, 193));
            togglePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, new Color(255,219,224), new Color(255,219,224), new Color(255,122,142), new Color(255,122,142)));
            lockIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/unlocked.png")));
            
        } else {
            // --------------------------------------
            // | BLUE THEME; when toggle turned OFF |
            // --------------------------------------
            
            key.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(204,255,255), new Color(204,255,255), new Color(0,153,153), new Color(0,153,153)));
            key.setSelectionColor(new Color(175,238,238));
            leftPanel.setBorder(BorderFactory.createLineBorder(new Color(140,234,234), 10));
            message.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(204,255,255), new Color(204,255,255), new Color(0,153,153), new Color(0,153,153)));
            message.setSelectionColor(new Color(175,238,238));
            if (message.getText().length() > 0 && key.getText().length() > 0 && processBtn.isEnabled()) {
                processBtnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(204,255,255), new Color(204,255,255), new Color(0,153,153), new Color(0,153,153)));
                processBtnPanel.setBackground(new Color(133,221,221));
            }
            togglePanel.setBackground(new Color(133,221,221));
            togglePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(204,255,255), new Color(204,255,255), new Color(0,153,153), new Color(0,153,153)));
            lockIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/somePackage/locked.png")));
            
        }
    }
    
    /**
     * encrypts or decrypts data
     * @param arrangementChars the arrangement characters
     * @param mergedArr the merged array so that the function can perform calculations
     *                   depending on whether encryption or decryption is selected
     * @param process [ true = encrypt ] | [ false = decrypt ]
     * @return the encrypted/ decrypted text
     */
    public String vigenereCipher(char[] arrangementChars, int[][] mergedArr, boolean process) {
        //final long startTime = System.currentTimeMillis(); // Stopwatch start time
        
        perTempCalc = 0;
        int[] indices = new int[mergedArr.length];
        if (process == true) {
            // ******************
            // **  ENCRYPTION  **
            // ******************
            //System.out.println(mergedArr.length);
            for (int i = 0; i < mergedArr.length; i++) {
                // Adding the values together that are found in the merged 2D array (MESSAGE & KEY) passed
                int sum = mergedArr[i][0] + mergedArr[i][1];
                if (sum > arrangementChars.length-1) { sum -= arrangementChars.length; } // subtract [arrangement] length from the sum if it is greater than the length
                indices[i] = sum;
                
                /**
                 * Setting the progress bar value
                 */
                //System.out.println("per total: " + percentageTotal);
                perTempCalc = ((double) i / (double) mergedArr.length * (double) progressBar.getMaximum());
                perTempCalc /= 4;
                //System.out.println("temp per: " + perTempCalc);
                progressBar.setValue((int) perTempCalc + (int) percentageTotal);
            }
            
        } else {
            // ******************
            // **  DECRYPTION  **
            // ******************
            for (int i = 0; i < mergedArr.length; i++) {
                // Subtracting the KEY's values from the MESSAGE's values that are found in the merged 2D array passed
                int sub = mergedArr[i][0] - mergedArr[i][1];
                if (sub < 0) { sub += arrangementChars.length; } // add [arrangement] length to the sub if it is lesser than 0
                indices[i] = sub;
                
                /**
                 * Setting the progress bar value
                 */
                //System.out.println("per total: " + percentageTotal);
                perTempCalc = ((double) i / (double) mergedArr.length * (double) progressBar.getMaximum());
                perTempCalc /= 4;
                //System.out.println("temp per: " + perTempCalc);
                progressBar.setValue((int) perTempCalc + (int) percentageTotal);
            }
        }
        
        percentageTotal *= 3;
        progressBar.setValue((int) percentageTotal);
        //System.out.println("totals: " + Arrays.toString(indices));
        
        //final long finishTime = System.currentTimeMillis(); // Stopwatch finish time
        //System.out.println("time taken for [  vigenereCipher()  ]: " + ((double) (finishTime - startTime) / 1000.0) + " seconds");
        
        return findChars(arrangementChars, indices); // Getting the characters of the index (sum)
    }
    
    // ==================
    // ||              ||
    // ||     MAIN     ||
    // ||              ||
    // ==================
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Metal look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* [IGNORE] If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(someForrm.class.getName()).log(java.util.logging.Level.SEVERE, "Look & Feel not loaded correctly", ex);
        }
        //</editor-fold>
        
        /* idk */
        System.out.println("program started running...");
        LOGGER.setLevel(Level.WARNING); // change to INFO to see INFO logs (currently it will only show WARNING logs and SEVERE logs)
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new someForrm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel charCounterLabel;
    private javax.swing.JSeparator charCounterSep;
    private javax.swing.JLabel copyrightLabel;
    private javax.swing.JToggleButton decryptionToggle;
    private javax.swing.JLabel exitBtn;
    private javax.swing.JLabel githubLogo;
    private javax.swing.JTextField key;
    private javax.swing.JLabel keyLabel;
    private javax.swing.JSeparator keySep;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JLabel lockIcon;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextArea message;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JScrollPane messagePanel;
    private javax.swing.JSeparator messageSep;
    private javax.swing.JLabel minimizeBtn;
    private javax.swing.JButton processBtn;
    private javax.swing.JPanel processBtnPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea resultText;
    private javax.swing.JScrollPane resultTextPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JButton saveBtn;
    private javax.swing.JPanel saveBtnPanel;
    private javax.swing.JLabel timeTakenLabel;
    private javax.swing.JLabel titleBottomSep;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel titleMiddleSep;
    private javax.swing.JLabel titleTopSep;
    private javax.swing.JPanel togglePanel;
    private javax.swing.JLabel twitterLogo;
    private javax.swing.JLabel versionLabel;
    private javax.swing.JLabel websiteLabel;
    // End of variables declaration//GEN-END:variables
}
