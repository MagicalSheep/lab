package cn.magicalsheep.ui;

import cn.magicalsheep.CallBackInterface;
import cn.magicalsheep.frontend.FlightHandler;
import cn.magicalsheep.frontend.TicketHandler;
import cn.magicalsheep.frontend.UserHandler;
import cn.magicalsheep.model.Plane;
import cn.magicalsheep.model.Ticket;
import cn.magicalsheep.model.User;
import cn.magicalsheep.utils.DateUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainFrame {
    private JPanel rootPanel;
    private JPanel leftPanel;
    private JPanel loginPanel;
    private JPanel userPanel;
    private JTextField accountField;
    private JPasswordField passwordField;
    private JButton login;
    private JButton register;
    private JTabbedPane tabbedPanel;
    private JPanel home;
    private JTextField fromField;
    private JTextField toField;
    private JTextField timeField;
    private JButton search;
    private JTable flightInfo;
    private JPanel info;
    private JLabel startTime;
    private JLabel endTime;
    private JLabel startAirplane;
    private JLabel endAirplane;
    private JLabel aircompany;
    private JLabel airplane;
    private JLabel date;
    private JLabel totalTime;
    private JPanel level1;
    private JLabel price1;
    private JButton buy1;
    private JSeparator separator3;
    private JPanel level2;
    private JButton buy2;
    private JLabel price2;
    private JSeparator separator4;
    private JPanel level3;
    private JButton buy3;
    private JLabel price3;
    private JPanel about;
    private JLabel name;
    private JLabel group;
    private JLabel money;
    private JButton addMoneyButton;
    private JPanel admin;
    private JButton addFlight;
    private JButton removeFlight;
    private JLabel totalPassengerNumber1;
    private JLabel remainPassengerNumber1;
    private JLabel totalPassengerNumber2;
    private JLabel remainPassengerNumber2;
    private JLabel totalPassengerNumber3;
    private JLabel remainPassengerNumber3;
    private JButton addUserTicket;
    private JButton removeUserTicket;
    private JTable myTickets;
    private JButton gitHubButton;
    private JTable adminFlightInfo;
    private JTable adminUserInfo;
    private JScrollPane adminFlightInfoPanel;
    private JScrollPane adminUserInfoPanel;
    private JButton logout;
    private final JPopupMenu myTicketMenu = new JPopupMenu();

    private int gr = 1;
    private String rightSelectFlightName = ""; // ??????????????????????????????????????????
    private String selectFlightName = ""; // ????????????????????????
    private String adminSelectFlightName = ""; // ????????????????????????????????????
    private String adminSelectTicketID = ""; // ????????????????????????????????????
    private boolean addFlightFlag = false; // ?????????????????????????????????????????????
    private boolean addUserTicketFlag = false; // ?????????????????????????????????????????????
    private final DefaultTableModel myTicketModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final DefaultTableModel searchResultModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final DefaultTableModel adminFlightInfoModel = new DefaultTableModel();
    private final DefaultTableModel adminUserInfoModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int col) {
            return col != 0;
        }
    };
    private final String[] myTicketsColumnNames = {"??????", "?????????", "?????????", "????????????", "????????????", "??????", "????????????"};
    private final String[] searchResultColumnNames = {"??????", "????????????", "????????????", "????????????", "????????????", "????????????", "?????????", "????????????", "??????"};
    private final String[] adminFlightInfoColumnNames = {"????????????", "??????", "?????????", "?????????", "????????????", "????????????", "????????????", "????????????", "???????????????", "?????????????????????", "???????????????", "???????????????", "?????????????????????", "???????????????", "???????????????", "?????????????????????", "???????????????"};
    private final String[] adminUserInfoColumnNames = {"??????ID", "????????????", "??????", "????????????", "?????????", "????????????", "????????????", "?????????", "?????????", "????????????", "????????????", "????????????", "????????????"};

    /**
     * ???????????????????????????
     */
    private void initUserInfo() {
        JMenuItem removeTicketItem = new JMenuItem("??????");
        removeTicketItem.addActionListener(e -> new Thread(() -> UserHandler.removeTicket(rightSelectFlightName, new CallBackInterface() {
            @Override
            public void success(Map<String, Object> params) {
                showUserInfo((User) params.get("user"));
                if (rightSelectFlightName.equals(selectFlightName))
                    showFlightInfo((Plane) params.get("plane"));
                JOptionPane.showMessageDialog(null, "????????????", "??????", JOptionPane.PLAIN_MESSAGE);
            }

            @Override
            public void failed(Map<String, Object> params) {
                JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
            }
        })).start());
        myTicketMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int rowAtPoint = myTickets.rowAtPoint(SwingUtilities.convertPoint(myTicketMenu, new Point(0, 0), myTickets));
                    if (rowAtPoint > -1) {
                        myTickets.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        rightSelectFlightName = myTickets.getValueAt(rowAtPoint, 0).toString();
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // ignore
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // ignore
            }
        });
        myTicketMenu.add(removeTicketItem);

        myTickets.setModel(myTicketModel);
        myTickets.setComponentPopupMenu(myTicketMenu);
        myTickets.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        myTicketModel.setColumnIdentifiers(myTicketsColumnNames);

        login.addActionListener(e -> {
            String account = accountField.getText();
            String pwd = new String(passwordField.getPassword());
            login.setEnabled(false);
            register.setEnabled(false);
            new Thread(() -> UserHandler.login(account, pwd, new CallBackInterface() {
                @Override
                public void success(Map<String, Object> params) {
                    User user = (User) params.get("user");
                    showUserInfo(user);
                    ((CardLayout) leftPanel.getLayout()).show(leftPanel, "userCard");
                    if (user.getGroup() == 0) {
                        tabbedPanel.add(admin, 2);
                        tabbedPanel.setTitleAt(2, "????????????");
                    }
                    login.setEnabled(true);
                    register.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                    login.setEnabled(true);
                    register.setEnabled(true);
                }
            })).start();
        });
        logout.addActionListener(e -> {
            logout.setEnabled(false);
            new Thread(() -> UserHandler.logout(new CallBackInterface() {
                @Override
                public void success(Map<String, Object> params) {
                    tabbedPanel.remove(admin);
                    ((CardLayout) leftPanel.getLayout()).show(leftPanel, "loginCard");
                    logout.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                    logout.setEnabled(true);
                }
            })).start();
        });
        register.addActionListener(e -> {
            String name = accountField.getText();
            String pwd = new String(passwordField.getPassword());
            login.setEnabled(false);
            register.setEnabled(false);
            Object[] groups = {"????????????", "?????????"};
            Object group = JOptionPane.showInputDialog(null, "????????????????????????????????????", "???????????????", JOptionPane.QUESTION_MESSAGE, null, groups, groups[0]);
            if (group == null) {
                login.setEnabled(true);
                register.setEnabled(true);
                return;
            }
            if (group.equals("?????????"))
                gr = 0;
            new Thread(() -> UserHandler.register(name, pwd, gr, new CallBackInterface() {
                @Override
                public void success(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, "???????????????????????????ID???" + params.get("id"), "????????????", JOptionPane.PLAIN_MESSAGE);
                    login.setEnabled(true);
                    register.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                    login.setEnabled(true);
                    register.setEnabled(true);
                }
            })).start();
        });
        addMoneyButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(null, "????????????????????????", "200");
            if (input == null)
                return;
            try {
                int m = Integer.parseInt(input);
                if (m <= 0) throw new NumberFormatException();
                new Thread(() -> UserHandler.addMoney(m, new CallBackInterface() {
                    @Override
                    public void success(Map<String, Object> params) {
                        money.setText(String.valueOf(params.get("money")));
                    }

                    @Override
                    public void failed(Map<String, Object> params) {
                        JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                    }
                })).start();
            } catch (NumberFormatException ee) {
                JOptionPane.showMessageDialog(null, "??????????????????????????????", "??????", JOptionPane.ERROR_MESSAGE);
            }
        });
        myTickets.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) return;
                int row = myTickets.getSelectedRow();
                if (selectFlightName.isEmpty()) {
                    tabbedPanel.add(info, 1);
                    tabbedPanel.setTitleAt(1, "????????????");
                }
                selectFlightName = myTickets.getValueAt(row, 0).toString();
                updateAndShowFlightDetailInfo();
            }
        });
    }

    /**
     * ???????????????????????????
     */
    private void initFlightSearch() {
        flightInfo.setModel(searchResultModel);
        searchResultModel.setColumnIdentifiers(searchResultColumnNames);

        flightInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) return;
                int row = flightInfo.getSelectedRow();
                if (selectFlightName.isEmpty()) {
                    tabbedPanel.add(info, 1);
                    tabbedPanel.setTitleAt(1, "????????????");
                }
                selectFlightName = flightInfo.getValueAt(row, 0).toString();
                updateAndShowFlightDetailInfo();
            }
        });
        search.addActionListener(e -> {
            Date date = DateUtils.getDate(timeField.getText(), "yyyy-MM-dd");
            String fromFieldText = fromField.getText();
            String toFieldText = toField.getText();
            updateSearchResult(fromFieldText, toFieldText, date);
        });
        timeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Date date = DateUtils.getDate(timeField.getText(), "yyyy-MM-dd");
                    String fromFieldText = fromField.getText();
                    String toFieldText = toField.getText();
                    updateSearchResult(fromFieldText, toFieldText, date);
                }
            }
        });
        timeField.setText(DateUtils.fromDate(new Date(), "yyyy-MM-dd"));
    }

    /**
     * ???????????????????????????
     */
    private void initFlightInfo() {
        buy1.addActionListener(e -> {
            buy1.setEnabled(false);
            new Thread(() -> UserHandler.buyTicket(selectFlightName, 1, new CallBackInterface() {
                @Override
                public void success(Map<String, Object> params) {
                    showUserInfo((User) params.get("user"));
                    showFlightInfo((Plane) params.get("plane"));
                    JOptionPane.showMessageDialog(null, "???????????????", "??????", JOptionPane.PLAIN_MESSAGE);
                    buy1.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                    buy1.setEnabled(true);
                }
            })).start();
        });
        buy2.addActionListener(e -> {
            buy2.setEnabled(false);
            new Thread(() -> UserHandler.buyTicket(selectFlightName, 2, new CallBackInterface() {
                @Override
                public void success(Map<String, Object> params) {
                    showUserInfo((User) params.get("user"));
                    showFlightInfo((Plane) params.get("plane"));
                    JOptionPane.showMessageDialog(null, "???????????????", "??????", JOptionPane.PLAIN_MESSAGE);
                    buy2.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                    buy2.setEnabled(true);
                }
            })).start();
        });
        buy3.addActionListener(e -> {
            buy3.setEnabled(false);
            new Thread(() -> UserHandler.buyTicket(selectFlightName, 3, new CallBackInterface() {
                @Override
                public void success(Map<String, Object> params) {
                    showUserInfo((User) params.get("user"));
                    showFlightInfo((Plane) params.get("plane"));
                    JOptionPane.showMessageDialog(null, "???????????????", "??????", JOptionPane.PLAIN_MESSAGE);
                    buy3.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                    buy3.setEnabled(true);
                }
            })).start();
        });
    }

    /**
     * ????????????????????????????????????
     */
    private void initAdminFlightInfo() {
        adminFlightInfo.setModel(adminFlightInfoModel);
        adminFlightInfoModel.setColumnIdentifiers(adminFlightInfoColumnNames);
        adminFlightInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        adminFlightInfoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = adminFlightInfo.getSelectedRow();
                if (selectedRow == -1) return;
                TableCellEditor editor = adminFlightInfo.getCellEditor();
                if (editor != null)
                    editor.stopCellEditing();
                adminFlightInfo.clearSelection();
                Plane plane = buildPlaneFromAdminFlightRow(selectedRow);
                new Thread(() -> FlightHandler.validate(plane, new CallBackInterface() {
                    @Override
                    public void success(Map<String, Object> params) {
                        if (addFlightFlag) {
                            new Thread(() -> FlightHandler.addFlight(plane, new CallBackInterface() {
                                @Override
                                public void success(Map<String, Object> params1) {
                                    updateAdminFlightInfo();
                                }

                                @Override
                                public void failed(Map<String, Object> params1) {
                                    updateAdminFlightInfo();
                                    JOptionPane.showMessageDialog(null, params1.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                                }
                            })).start();
                            addFlightFlag = false;
                        } else {
                            new Thread(() -> FlightHandler.modifyFlight(plane, new CallBackInterface() {
                                @Override
                                public void success(Map<String, Object> params1) {
                                    updateAdminFlightInfo();
                                }

                                @Override
                                public void failed(Map<String, Object> params1) {
                                    updateAdminFlightInfo();
                                }
                            })).start();
                        }
                    }

                    @Override
                    public void failed(Map<String, Object> params) {
                        updateAdminFlightInfo();
                        addFlightFlag = false;
                        JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                    }
                })).start();
            }
        });
        adminFlightInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = adminFlightInfo.getSelectedRow();
                adminSelectFlightName = adminFlightInfo.getValueAt(row, 0)
                        + " " + adminFlightInfo.getValueAt(row, 1);
            }
        });
        addFlight.addActionListener(e -> {
            Object[] newInfo = new Object[adminFlightInfoColumnNames.length];
            SwingUtilities.invokeLater(() -> {
                adminFlightInfoModel.addRow(newInfo);
                adminFlightInfoModel.fireTableDataChanged();
                adminFlightInfo.updateUI();
                int index = adminFlightInfoModel.getRowCount() - 1;
                adminFlightInfo.setRowSelectionInterval(index, index);
            });
            addFlightFlag = true;
        });
        removeFlight.addActionListener(e -> FlightHandler.removeFlight(adminSelectFlightName, new CallBackInterface() {
            @Override
            public void success(Map<String, Object> params) {
                updateAdminFlightInfo();
                updateAdminUserInfo();
                updateUserInfo();
            }

            @Override
            public void failed(Map<String, Object> params) {
                JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
            }
        }));
    }

    /**
     * ??????????????????????????????????????????
     */
    private void initAdminUserInfo() {
        adminUserInfo.setModel(adminUserInfoModel);
        adminUserInfoModel.setColumnIdentifiers(adminUserInfoColumnNames);
        adminUserInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        adminUserInfoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectRow = adminUserInfo.getSelectedRow();
                if (selectRow == -1) return;
                TableCellEditor editor = adminUserInfo.getCellEditor();
                if (editor != null)
                    editor.stopCellEditing();
                adminUserInfo.clearSelection();
                if (addUserTicketFlag) {
                    String account = String.valueOf(adminUserInfoModel.getValueAt(selectRow, 3));
                    String planeName = adminUserInfoModel.getValueAt(selectRow, 1)
                            + " " + adminUserInfoModel.getValueAt(selectRow, 2);
                    String levelStr = String.valueOf(adminUserInfo.getValueAt(selectRow, 5));
                    int level = switch (levelStr) {
                        case "null" -> -1;
                        case "?????????" -> 1;
                        case "?????????" -> 2;
                        case "?????????" -> 3;
                        default -> 0;
                    };
                    if (level == -1) {
                        updateAdminUserInfo();
                        return;
                    }
                    if (level == 0) {
                        updateAdminUserInfo();
                        addUserTicketFlag = false;
                        JOptionPane.showMessageDialog(null, "????????????????????????", "??????", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    new Thread(() -> UserHandler.buyTicket(account, planeName, level, new CallBackInterface() {
                        @Override
                        public void success(Map<String, Object> params) {
                            updateAdminUserInfo();
                            updateUserInfo();
                            updateAdminFlightInfo();
                        }

                        @Override
                        public void failed(Map<String, Object> params) {
                            updateAdminUserInfo();
                            JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                        }
                    })).start();
                    addUserTicketFlag = false;
                } else {
                    updateAdminUserInfo();
                }
            }
        });
        adminUserInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = adminUserInfo.getSelectedRow();
                adminSelectTicketID = String.valueOf(adminUserInfo.getValueAt(row, 0));
            }
        });
        addUserTicket.addActionListener(e -> {
            Object[] newInfo = new Object[adminUserInfoColumnNames.length];
            SwingUtilities.invokeLater(() -> {
                adminUserInfoModel.addRow(newInfo);
                adminUserInfoModel.fireTableDataChanged();
                int index = adminUserInfoModel.getRowCount() - 1;
                adminUserInfo.setRowSelectionInterval(index, index);
                adminUserInfo.updateUI();
            });
            addUserTicketFlag = true;
        });
        removeUserTicket.addActionListener(e -> {
            if (adminSelectTicketID.isEmpty())
                return;
            new Thread(() -> TicketHandler.removeTicket(Integer.parseInt(adminSelectTicketID), new CallBackInterface() {
                @Override
                public void success(Map<String, Object> params) {
                    updateAdminFlightInfo();
                    updateAdminUserInfo();
                    updateUserInfo();
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
                }
            })).start();
        });
    }

    /**
     * ??????????????????
     */
    private void initTabbedPane() {
        tabbedPanel.remove(info);
        tabbedPanel.remove(admin);

        tabbedPanel.addChangeListener(e -> {
            if (tabbedPanel.getSelectedComponent().equals(admin)) {
                updateAdminFlightInfo();
                updateAdminUserInfo();
            }
        });
    }

    public MainFrame() {
        initUserInfo();
        initFlightSearch();
        initFlightInfo();
        initAdminFlightInfo();
        initAdminUserInfo();
        initTabbedPane();

        gitHubButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/MagicalSheep"));
            } catch (Exception ex) {
                // ignore
            }
        });
    }

    /**
     * ?????????????????????????????????
     *
     * @param user ????????????
     */
    private void showUserInfo(User user) {
        myTicketModel.getDataVector().clear();
        name.setText(user.getName());
        if (user.getGroup() == 0)
            group.setText("?????????");
        else
            group.setText("????????????");
        money.setText(String.valueOf(user.getMoney()));
        Map<String, Ticket> ticketList = user.getTicketList();
        for (Ticket ticket : ticketList.values()) {
            Object[] info = new Object[myTicketsColumnNames.length];
            info[0] = ticket.getCompany() + " " + ticket.getName();
            info[1] = ticket.getFrom();
            info[2] = ticket.getTo();
            info[3] = DateUtils.fromDate(ticket.getStartTime());
            info[4] = DateUtils.fromDate(ticket.getEndTime());
            info[5] = ticket.getPrice();
            info[6] = ticket.getLevel();
            myTicketModel.addRow(info);
        }
        myTicketModel.fireTableDataChanged();
        myTickets.updateUI();
    }

    /**
     * ????????????????????????????????????
     *
     * @param plane ????????????
     */
    private void showFlightInfo(Plane plane) {
        level1.setVisible(true);
        level2.setVisible(true);
        level3.setVisible(true);
        separator3.setVisible(true);
        separator4.setVisible(true);
        buy1.setEnabled(true);
        buy2.setEnabled(true);
        buy3.setEnabled(true);
        aircompany.setText(plane.getCompany());
        airplane.setText(plane.getName());
        date.setText(DateUtils.fromDate(plane.getStartTime(), "MM???dd??? E"));
        long deltaTime = plane.getEndTime().getTime() - plane.getStartTime().getTime();
        long day = TimeUnit.MILLISECONDS.toDays(deltaTime);
        long hours = TimeUnit.MILLISECONDS.toHours(deltaTime)
                - TimeUnit.DAYS.toHours(day);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(deltaTime)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(deltaTime));
        totalTime.setText((day == 0) ? ("???" + hours + "???" + minutes + "???") : ("???" + day + "???" + hours + "???" + minutes + "???"));
        startTime.setText(DateUtils.fromDate(plane.getStartTime(), "HH:mm"));
        endTime.setText(DateUtils.fromDate(plane.getEndTime(), "HH:mm"));
        startAirplane.setText(plane.getStartPlace());
        endAirplane.setText(plane.getEndPlace());
        if (plane.getLevel1TicketNumber() == 0) {
            level1.setVisible(false);
            separator3.setVisible(false);
        } else {
            price1.setText(String.valueOf(plane.getLevel1Price()));
            totalPassengerNumber1.setText(String.valueOf(plane.getLevel1TicketNumber()));
            remainPassengerNumber1.setText(String.valueOf(plane.getRemainLevel1TicketNumber()));
        }
        if (plane.getLevel2TicketNumber() == 0) {
            level2.setVisible(false);
            separator4.setVisible(false);
        } else {
            price2.setText(String.valueOf(plane.getLevel2Price()));
            totalPassengerNumber2.setText(String.valueOf(plane.getLevel2TicketNumber()));
            remainPassengerNumber2.setText(String.valueOf(plane.getRemainLevel2TicketNumber()));
        }
        if (plane.getLevel3TicketNumber() == 0) {
            level3.setVisible(false);
            separator4.setVisible(false);
        } else {
            price3.setText(String.valueOf(plane.getLevel3Price()));
            totalPassengerNumber3.setText(String.valueOf(plane.getLevel3TicketNumber()));
            remainPassengerNumber3.setText(String.valueOf(plane.getRemainLevel3TicketNumber()));
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param result ????????????
     */
    private void showSearchResult(List<Plane> result) {
        searchResultModel.getDataVector().clear();
        for (Plane plane : result) {
            Object[] info = new Object[searchResultColumnNames.length];
            info[0] = plane.getCompany() + " " + plane.getName();
            info[1] = plane.getStartPlace();
            info[2] = plane.getEndPlace();
            info[3] = DateUtils.fromDate(plane.getStartTime());
            info[4] = DateUtils.fromDate(plane.getEndTime());
            String levels = "";
            int lowestPrice = 0x7fffffff;
            if (plane.getLevel1TicketNumber() != 0) {
                levels += "????????? ";
                lowestPrice = plane.getLevel1Price();
            }
            if (plane.getLevel2TicketNumber() != 0) {
                levels += "????????? ";
                lowestPrice = Math.min(lowestPrice, plane.getLevel2Price());
            }
            if (plane.getLevel3TicketNumber() != 0) {
                levels += "????????? ";
                lowestPrice = Math.min(lowestPrice, plane.getLevel3Price());
            }
            info[5] = levels;
            info[6] = plane.getLevel1TicketNumber() + plane.getLevel2TicketNumber() + plane.getLevel3TicketNumber();
            info[7] = plane.getRemainLevel1TicketNumber() + plane.getRemainLevel2TicketNumber() + plane.getRemainLevel3TicketNumber();
            info[8] = lowestPrice;
            searchResultModel.addRow(info);
        }
        searchResultModel.fireTableDataChanged();
        flightInfo.updateUI();
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param planes ??????????????????
     */
    private void showAdminFlightInfo(List<Plane> planes) {
        adminFlightInfoModel.getDataVector().clear();
        for (Plane plane : planes) {
            Object[] info = new Object[adminFlightInfoColumnNames.length];
            info[0] = plane.getCompany();
            info[1] = plane.getName();
            info[2] = plane.getFrom();
            info[3] = plane.getTo();
            info[4] = plane.getStartPlace();
            info[5] = plane.getEndPlace();
            info[6] = DateUtils.fromDate(plane.getStartTime());
            info[7] = DateUtils.fromDate(plane.getEndTime());
            info[8] = plane.getLevel1TicketNumber();
            info[9] = plane.getRemainLevel1TicketNumber();
            info[10] = plane.getLevel1Price();
            info[11] = plane.getLevel2TicketNumber();
            info[12] = plane.getRemainLevel2TicketNumber();
            info[13] = plane.getLevel2Price();
            info[14] = plane.getLevel3TicketNumber();
            info[15] = plane.getRemainLevel3TicketNumber();
            info[16] = plane.getLevel3Price();
            adminFlightInfoModel.addRow(info);
        }
        adminFlightInfoModel.fireTableDataChanged();
        adminFlightInfo.updateUI();
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param tickets ??????????????????
     */
    private void showAdminUserInfo(List<Ticket> tickets) {
        adminUserInfoModel.getDataVector().clear();
        for (Ticket ticket : tickets) {
            Object[] info = new Object[adminUserInfoColumnNames.length];
            info[0] = ticket.getId();
            info[1] = ticket.getCompany();
            info[2] = ticket.getName();
            info[3] = ticket.getOwnerID();
            info[4] = ticket.getOwnerName();
            info[5] = ticket.getLevel();
            info[6] = ticket.getPrice();
            info[7] = ticket.getFrom();
            info[8] = ticket.getTo();
            info[9] = ticket.getStartPlace();
            info[10] = ticket.getEndPlace();
            info[11] = DateUtils.fromDate(ticket.getStartTime());
            info[12] = DateUtils.fromDate(ticket.getEndTime());
            adminUserInfoModel.addRow(info);
        }
        adminUserInfoModel.fireTableDataChanged();
        adminUserInfo.updateUI();
    }

    /**
     * ????????????????????????????????????????????????????????????
     * ??????????????????????????????
     *
     * @param index ???????????????
     * @return ????????????
     */
    private Plane buildPlaneFromAdminFlightRow(int index) {
        try {
            return Plane.builder()
                    .company(String.valueOf(adminFlightInfoModel.getValueAt(index, 0)))
                    .name(String.valueOf(adminFlightInfoModel.getValueAt(index, 1)))
                    .from(String.valueOf(adminFlightInfoModel.getValueAt(index, 2)))
                    .to(String.valueOf(adminFlightInfoModel.getValueAt(index, 3)))
                    .startPlace(String.valueOf(adminFlightInfoModel.getValueAt(index, 4)))
                    .endPlace(String.valueOf(adminFlightInfoModel.getValueAt(index, 5)))
                    .startTime(DateUtils.getDate(String.valueOf(adminFlightInfoModel.getValueAt(index, 6)), "yyyy-MM-dd HH:mm"))
                    .endTime(DateUtils.getDate(String.valueOf(adminFlightInfoModel.getValueAt(index, 7)), "yyyy-MM-dd HH:mm"))
                    .level1TicketNumber(Integer.parseInt(String.valueOf(adminFlightInfoModel.getValueAt(index, 8))))
                    .remainLevel1TicketNumber(Integer.parseInt(String.valueOf(adminFlightInfoModel.getValueAt(index, 9))))
                    .level1Price(Integer.parseInt(String.valueOf(adminFlightInfoModel.getValueAt(index, 10))))
                    .level2TicketNumber(Integer.parseInt(String.valueOf(adminFlightInfoModel.getValueAt(index, 11))))
                    .remainLevel2TicketNumber(Integer.parseInt(String.valueOf(adminFlightInfoModel.getValueAt(index, 12))))
                    .level2Price(Integer.parseInt(String.valueOf(adminFlightInfoModel.getValueAt(index, 13))))
                    .level3TicketNumber(Integer.parseInt(String.valueOf(adminFlightInfoModel.getValueAt(index, 14))))
                    .remainLevel3TicketNumber(Integer.parseInt(String.valueOf(adminFlightInfoModel.getValueAt(index, 15))))
                    .level3Price(Integer.parseInt(String.valueOf(adminFlightInfoModel.getValueAt(index, 16))))
                    .build();
        } catch (Exception e) {
            return new Plane();
        }
    }

    /**
     * ???UI??????????????????????????????????????????????????????
     */
    @SuppressWarnings("unchecked")
    private void updateAdminFlightInfo() {
        new Thread(() -> FlightHandler.getFlightInfo(new CallBackInterface() {
            @Override
            public void success(Map<String, Object> params) {
                showAdminFlightInfo((List<Plane>) params.get("res"));
            }

            @Override
            public void failed(Map<String, Object> params) {
                JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    /**
     * ???UI?????????????????????????????????????????????????????????
     */
    @SuppressWarnings("unchecked")
    private void updateAdminUserInfo() {
        new Thread(() -> TicketHandler.getTicketInfo(new CallBackInterface() {
            @Override
            public void success(Map<String, Object> params) {
                showAdminUserInfo((List<Ticket>) params.get("res"));
            }

            @Override
            public void failed(Map<String, Object> params) {
                JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    /**
     * ???UI??????????????????????????????????????????
     *
     * @param from ?????????
     * @param to   ?????????
     * @param date ????????????
     */
    @SuppressWarnings("unchecked")
    private void updateSearchResult(String from, String to, Date date) {
        new Thread(() -> FlightHandler.search(from, to, date, new CallBackInterface() {
            @Override
            public void success(Map<String, Object> params) {
                showSearchResult((List<Plane>) params.get("result"));
            }

            @Override
            public void failed(Map<String, Object> params) {
                JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    /**
     * ???UI??????????????????????????????????????????
     */
    private void updateUserInfo() {
        new Thread(() -> UserHandler.getUserInfo(new CallBackInterface() {
            @Override
            public void success(Map<String, Object> params) {
                showUserInfo((User) params.get("user"));
            }

            @Override
            public void failed(Map<String, Object> params) {
                JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    /**
     * ???UI??????????????????????????????????????????
     */
    private void updateAndShowFlightDetailInfo() {
        new Thread(() -> FlightHandler.getFlightInfo(selectFlightName, new CallBackInterface() {
            @Override
            public void success(Map<String, Object> params) {
                showFlightInfo((Plane) params.get("plane"));
                tabbedPanel.setSelectedIndex(1);
            }

            @Override
            public void failed(Map<String, Object> params) {
                JOptionPane.showMessageDialog(null, params.get("msg"), "??????", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
