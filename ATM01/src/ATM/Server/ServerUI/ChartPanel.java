package ATM.Server.ServerUI;

import ATM.Server.service;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ChartPanel extends JPanel {
    private service atmService;
    private MainFrame mainFrame;
    private org.jfree.chart.ChartPanel chartPanel;
    private JButton refreshButton;
    private JComboBox<String> chartTypeCombo;
    private JLabel statsLabel;

    public ChartPanel(service atmService, MainFrame mainFrame) {
        this.atmService = atmService;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        initializeUI();
        createChart();
    }

    //初始化界面

    private void initializeUI() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("百川银行 数据统计分析", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel chartPanel = new JPanel(new FlowLayout());
        JLabel typeLabel = new JLabel("图表类型");
        chartTypeCombo = new JComboBox<>(new String[]{"账户余额分布", "账户统计概览", "余额区间统计"});
        refreshButton = new JButton("刷新数据");

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshChart();
                mainFrame.logMessage("数据统计图已刷新");
            }
        });

        chartTypeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createChart();
            }
        });

        chartPanel.add(typeLabel);
        chartPanel.add(chartTypeCombo);
        chartPanel.add(refreshButton);
        topPanel.add(chartPanel, BorderLayout.CENTER);

        statsLabel = new JLabel();
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        statsLabel.setBorder(BorderFactory.createEmptyBorder(3, 80, 3, 80));
        topPanel.add(statsLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        updateStatsLabel();

    }

    //绘图
    private void createChart() {
        String selectedType = (String) chartTypeCombo.getSelectedItem();

        JFreeChart chart;
        switch (selectedType) {
            case "账户余额分布":
                chart = BalanceDistributionChart();
                break;
            case "账户统计概览":
                chart = AccountStatisticsChart();
                break;
            case "余额区间统计":
                chart = BalanceRangeChart();
                break;
            default:
                chart = BalanceDistributionChart();
        }

        if (chartPanel != null) {
            remove(chartPanel);
        }

        chartPanel = new org.jfree.chart.ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        add(chartPanel, BorderLayout.CENTER);

        updateStatsLabel();
        revalidate();
        repaint();
    }

    //账户余额分布
    private JFreeChart BalanceDistributionChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> distribution = atmService.getBalanceDistribution();

        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            dataset.addValue(entry.getValue(), "账户数量", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart3D("账户余额分布", "余额区间", "账户数量", dataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(197, 197, 197));
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);

        BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
        Color barColor = new Color(70, 100, 130, 255);
        renderer.setSeriesPaint(0, barColor);

        renderer.setWallPaint(new Color(197, 197, 197)); // 墙面颜色与背景一致
        renderer.setItemMargin(0.3); // 柱体间距
        renderer.setMaximumBarWidth(0.10);

        // 设置字体
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 14));
        plot.getDomainAxis().setLabelFont(new Font("微软雅黑", Font.PLAIN, 11));
        plot.getRangeAxis().setLabelFont(new Font("微软雅黑", Font.PLAIN, 11));

        // 设置坐标轴刻度字体
        plot.getDomainAxis().setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 10));
        plot.getRangeAxis().setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 10));

        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(new Font("微软雅黑", Font.PLAIN, 10));
        }

        plot.getDomainAxis().setAxisLinePaint(Color.GRAY);
        plot.getRangeAxis().setAxisLinePaint(Color.GRAY);

        return chart;


    }

    //账户统计
    private JFreeChart AccountStatisticsChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//创建数据集
        Map<String, Double> allBalances = atmService.getAllAccountBalances();


        for (Map.Entry<String, Double> entry : allBalances.entrySet()) {
            String accountId = entry.getKey();
            Double balance = entry.getValue();
       //系列名
            dataset.addValue(balance, "账户余额", accountId);
        }

        JFreeChart chart = ChartFactory.createBarChart3D("各账户余额统计", "账户", "余额", dataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(197, 197, 197));
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);

        BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
        Color barColor = new Color(70, 100, 130, 255);
        renderer.setSeriesPaint(0, barColor);

        renderer.setWallPaint(new Color(197, 197, 197)); // 墙面颜色与背景一致
        renderer.setItemMargin(0.3); // 柱体间距
        renderer.setMaximumBarWidth(0.10);

// 设置字体
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 14));
        plot.getDomainAxis().setLabelFont(new Font("微软雅黑", Font.PLAIN, 11));
        plot.getRangeAxis().setLabelFont(new Font("微软雅黑", Font.PLAIN, 11));

        // 设置坐标轴刻度字体
        plot.getDomainAxis().setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 10));
        plot.getRangeAxis().setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 10));

        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(new Font("微软雅黑", Font.PLAIN, 10));
        }
        plot.getDomainAxis().setAxisLinePaint(Color.GRAY);
        plot.getRangeAxis().setAxisLinePaint(Color.GRAY);
        return chart;
    }

    //余额区间统计
    private JFreeChart BalanceRangeChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> distribution = atmService.getBalanceDistribution();

        // 计算百分比
        int total = atmService.getTotalAccounts();
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            double percentage = total > 0 ? (entry.getValue() * 100.0 / total) : 0;
            dataset.addValue(percentage, "占比(%)", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart3D(
                "账户余额区间占比",
                "余额区间(元)",
                "占比(%)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(197, 197, 197));
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);

        BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
        Color barColor = new Color(70, 100, 130, 255);
        renderer.setSeriesPaint(0, barColor);

        renderer.setWallPaint(new Color(197, 197, 197)); // 墙面颜色与背景一致
        renderer.setItemMargin(0.3); // 柱体间距
        renderer.setMaximumBarWidth(0.10);

        // 设置字体
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 14));
        plot.getDomainAxis().setLabelFont(new Font("微软雅黑", Font.PLAIN, 11));
        plot.getRangeAxis().setLabelFont(new Font("微软雅黑", Font.PLAIN, 11));

        // 设置坐标轴刻度字体
        plot.getDomainAxis().setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 10));
        plot.getRangeAxis().setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 10));

        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(new Font("微软雅黑", Font.PLAIN, 10));
        }
        plot.getDomainAxis().setAxisLinePaint(Color.GRAY);
        plot.getRangeAxis().setAxisLinePaint(Color.GRAY);
        return chart;
    }

    //统计数据总览
    private void updateStatsLabel() {
        int totalAccounts = atmService.getTotalAccounts();
        double totalAssets = atmService.getTotalAssets();
        double avgBalance = atmService.getAverageBalance();

        String statsText = String.format("数据概览：总账户数: %d  |  总资产: %.2f 元  |  平均余额: %.2f 元", totalAccounts, totalAssets, avgBalance);

        statsLabel.setText(statsText);
    }


    private void refreshChart() {
        createChart();
    }

    public void refreshData() {
        refreshChart();
    }
}
