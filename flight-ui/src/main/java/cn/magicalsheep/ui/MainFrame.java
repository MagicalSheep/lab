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
    private String rightSelectFlightName = ""; // 个人中心已购票列表右键所选项
    private String selectFlightName = ""; // 搜索结果页已选项
    private String adminSelectFlightName = ""; // 管理面板航班信息页已选项
    private String adminSelectTicketID = ""; // 管理面板机票信息页已选项
    private boolean addFlightFlag = false; // 管理面板航班信息页新添信息标记
    private boolean addUserTicketFlag = false; // 管理面板机票信息页新添信息标记
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
    private final String[] myTicketsColumnNames = {"航班", "始发地", "终点地", "起飞时间", "到达时间", "票价", "舱位等级"};
    private final String[] searchResultColumnNames = {"航班", "始发机场", "终点机场", "起飞时间", "到达时间", "舱位等级", "总票数", "剩余票数", "售价"};
    private final String[] adminFlightInfoColumnNames = {"航空公司", "机型", "始发地", "终点地", "始发机场", "终点机场", "起飞时间", "到达时间", "经济舱票数", "经济舱剩余票数", "经济舱价格", "公务舱票数", "公务舱剩余票数", "公务舱价格", "头等舱票数", "头等舱剩余票数", "头等舱价格"};
    private final String[] adminUserInfoColumnNames = {"机票ID", "航空公司", "机型", "用户账号", "用户名", "舱位等级", "机票价格", "始发地", "终点地", "始发机场", "终点机场", "起飞时间", "到达时间"};

    /**
     * 初始化个人中心页面
     */
    private void initUserInfo() {
        JMenuItem removeTicketItem = new JMenuItem("退票");
        removeTicketItem.addActionListener(e -> new Thread(() -> UserHandler.removeTicket(rightSelectFlightName, new CallBackInterface() {
            @Override
            public void success(Map<String, Object> params) {
                showUserInfo((User) params.get("user"));
                if (rightSelectFlightName.equals(selectFlightName))
                    showFlightInfo((Plane) params.get("plane"));
                JOptionPane.showMessageDialog(null, "退票成功", "提示", JOptionPane.PLAIN_MESSAGE);
            }

            @Override
            public void failed(Map<String, Object> params) {
                JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
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
                        tabbedPanel.setTitleAt(2, "管理面板");
                    }
                    login.setEnabled(true);
                    register.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
                    logout.setEnabled(true);
                }
            })).start();
        });
        register.addActionListener(e -> {
            String name = accountField.getText();
            String pwd = new String(passwordField.getPassword());
            login.setEnabled(false);
            register.setEnabled(false);
            Object[] groups = {"普通用户", "管理员"};
            Object group = JOptionPane.showInputDialog(null, "请选择该用户所属的用户组", "用户组选择", JOptionPane.QUESTION_MESSAGE, null, groups, groups[0]);
            if (group == null) {
                login.setEnabled(true);
                register.setEnabled(true);
                return;
            }
            if (group.equals("管理员"))
                gr = 0;
            new Thread(() -> UserHandler.register(name, pwd, gr, new CallBackInterface() {
                @Override
                public void success(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, "注册成功，你的用户ID为" + params.get("id"), "注册成功", JOptionPane.PLAIN_MESSAGE);
                    login.setEnabled(true);
                    register.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
                    login.setEnabled(true);
                    register.setEnabled(true);
                }
            })).start();
        });
        addMoneyButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(null, "请输入充值金额：", "200");
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
                        JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                })).start();
            } catch (NumberFormatException ee) {
                JOptionPane.showMessageDialog(null, "请输入有效的正整数！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        myTickets.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) return;
                int row = myTickets.getSelectedRow();
                if (selectFlightName.isEmpty()) {
                    tabbedPanel.add(info, 1);
                    tabbedPanel.setTitleAt(1, "机票信息");
                }
                selectFlightName = myTickets.getValueAt(row, 0).toString();
                updateAndShowFlightDetailInfo();
            }
        });
    }

    /**
     * 初始化航班搜索页面
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
                    tabbedPanel.setTitleAt(1, "机票信息");
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
     * 初始化航班信息页面
     */
    private void initFlightInfo() {
        buy1.addActionListener(e -> {
            buy1.setEnabled(false);
            new Thread(() -> UserHandler.buyTicket(selectFlightName, 1, new CallBackInterface() {
                @Override
                public void success(Map<String, Object> params) {
                    showUserInfo((User) params.get("user"));
                    showFlightInfo((Plane) params.get("plane"));
                    JOptionPane.showMessageDialog(null, "购票成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                    buy1.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "购票成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                    buy2.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "购票成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                    buy3.setEnabled(true);
                }

                @Override
                public void failed(Map<String, Object> params) {
                    JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
                    buy3.setEnabled(true);
                }
            })).start();
        });
    }

    /**
     * 初始化管理面板航班信息页
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
                                    JOptionPane.showMessageDialog(null, params1.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
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
                        JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }));
    }

    /**
     * 初始化管理面板用户已购票页面
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
                        case "经济舱" -> 1;
                        case "公务舱" -> 2;
                        case "头等舱" -> 3;
                        default -> 0;
                    };
                    if (level == -1) {
                        updateAdminUserInfo();
                        return;
                    }
                    if (level == 0) {
                        updateAdminUserInfo();
                        addUserTicketFlag = false;
                        JOptionPane.showMessageDialog(null, "舱位等级不存在！", "错误", JOptionPane.ERROR_MESSAGE);
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
                            JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
                }
            })).start();
        });
    }

    /**
     * 初始化标签页
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
     * 在个人中心显示用户信息
     *
     * @param user 用户实体
     */
    private void showUserInfo(User user) {
        myTicketModel.getDataVector().clear();
        name.setText(user.getName());
        if (user.getGroup() == 0)
            group.setText("管理员");
        else
            group.setText("普通用户");
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
     * 在航班信息页显示航班信息
     *
     * @param plane 航班实体
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
        date.setText(DateUtils.fromDate(plane.getStartTime(), "MM月dd日 E"));
        long deltaTime = plane.getEndTime().getTime() - plane.getStartTime().getTime();
        long day = TimeUnit.MILLISECONDS.toDays(deltaTime);
        long hours = TimeUnit.MILLISECONDS.toHours(deltaTime)
                - TimeUnit.DAYS.toHours(day);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(deltaTime)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(deltaTime));
        totalTime.setText((day == 0) ? ("共" + hours + "时" + minutes + "分") : ("共" + day + "天" + hours + "时" + minutes + "分"));
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
     * 在搜索结果页显示航班列表
     *
     * @param result 航班列表
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
                levels += "经济舱 ";
                lowestPrice = plane.getLevel1Price();
            }
            if (plane.getLevel2TicketNumber() != 0) {
                levels += "公务舱 ";
                lowestPrice = Math.min(lowestPrice, plane.getLevel2Price());
            }
            if (plane.getLevel3TicketNumber() != 0) {
                levels += "头等舱 ";
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
     * 在管理面板航班信息页显示航班信息列表
     *
     * @param planes 航班信息列表
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
     * 在管理面板用户已购票页显示机票信息列表
     *
     * @param tickets 机票信息列表
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
     * 从管理面板航班信息页已选项中构建航班实体
     * 构建失败返回默认实体
     *
     * @param index 已选项索引
     * @return 航班实体
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
     * 向UI后端申请数据并更新管理面板航班信息页
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
                JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    /**
     * 向UI后端申请数据并更新管理面板用户已购票页
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
                JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    /**
     * 向UI后端申请数据并更新搜索结果页
     *
     * @param from 始发地
     * @param to   终点地
     * @param date 出发时间
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
                JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    /**
     * 向UI后端申请数据并更新个人中心页
     */
    private void updateUserInfo() {
        new Thread(() -> UserHandler.getUserInfo(new CallBackInterface() {
            @Override
            public void success(Map<String, Object> params) {
                showUserInfo((User) params.get("user"));
            }

            @Override
            public void failed(Map<String, Object> params) {
                JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    /**
     * 向UI后端申请数据并更新航班详情页
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
                JOptionPane.showMessageDialog(null, params.get("msg"), "错误", JOptionPane.ERROR_MESSAGE);
            }
        })).start();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
