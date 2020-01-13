/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.schedule.main;

import com.health.objects.GetAvailibleInsurances;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Inspiron
 */
public class Util {

    public static class Obj {

        public Object userData;
        public String name;

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Obj) {
                if (((Obj) o).name.equals(name) && ((Obj) o).userData == userData) {
                    return true;
                }
            }
            return false;
        }
    }

    public static String[] addToArray(String obj, String... t) {
        String[] g = new String[t.length + 1];

        for (int y = 0; y < t.length; y++) {
            g[y] = t[y];
        }
        g[g.length - 1] = obj;
        return g;
    }

    public static void reload(JTree jTree1) {

        List<TreePath> expanded = new ArrayList<>();

        for (int i = 0; i < jTree1.getRowCount() - 1; i++) {
            TreePath currPath = jTree1.getPathForRow(i);
            TreePath nextPath = jTree1.getPathForRow(i + 1);
            if (currPath.isDescendant(nextPath)) {
                expanded.add(currPath);
            }
        }
        ((DefaultTreeModel) jTree1.getModel()).reload();
        for (TreePath path : expanded) {
            jTree1.expandPath(path);
        }

    }

    public static GetAvailibleInsurances.Insurance makeCopy(GetAvailibleInsurances.Insurance in) {

        GetAvailibleInsurances.Insurance i = new GetAvailibleInsurances.Insurance();
        i.insurance_id = in.insurance_id;
        i.insurance_path = in.insurance_path;
        i.percentage = in.percentage;
        return i;
    }

    public static void remove_node(DefaultMutableTreeNode root, Object o) {
        DefaultMutableTreeNode node = findNode(root, o);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            if (par != null) {
                node.removeFromParent();
                cleanNode(par);
            }
        }
    }

    private static void cleanNode(DefaultMutableTreeNode node) {
        if (node.getChildCount() == 0) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            if (par != null) {
                node.removeFromParent();
                cleanNode(par);
            }
        }
    }

    public static DefaultMutableTreeNode getRoomsNodes() {

        ArrayList<String> paths = new ArrayList<>(Main.rooms.size());

        for (int i = 0; i < Main.rooms.size(); i++) {
            paths.add(Main.rooms.get(i).room_path);
        }

        return Util.generateNodes(paths, Main.rooms);

    }

    public static DefaultMutableTreeNode getServiceNodes() {

        ArrayList<String> paths = new ArrayList<>(Main.services.size());

        for (int i = 0; i < Main.services.size(); i++) {
            paths.add(Main.services.get(i).service_path);
        }

        return Util.generateNodes(paths, Main.services);

    }

    public static DefaultMutableTreeNode findNode(DefaultMutableTreeNode node, Object o) {

        try {
            if (((Obj) node.getUserObject()).userData.equals(o)) {
                return node;
            }
        } catch (Exception e) {
        }
        DefaultMutableTreeNode found = null;
        for (int i = 0; i < node.getChildCount(); i++) {
            if (found == null) {
                found = findNode((DefaultMutableTreeNode) node.getChildAt(i), o);
            } else {
                break;
            }
        }

        return found;
    }

    public static DefaultMutableTreeNode add_node(DefaultMutableTreeNode root, String path, Object object) {

        DefaultMutableTreeNode current = root;
        String[] splits = path.split("/");
        int idx = 0;
        for (String name : splits) {
            Enumeration en = current.children();
            boolean found = false;
            Obj o = new Obj();
            o.name = name;
            o.userData = (idx == splits.length - 1) ? object : null;

            DefaultMutableTreeNode node = null;
            while (en.hasMoreElements()) {
                if ((node = (DefaultMutableTreeNode) en.nextElement()).getUserObject().toString().equals(name)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode(o);
                node = n;
                current.add(node);
            } else {
                node.setUserObject(o);
            }
            idx++;
            current = node;
        }

        return current;
    }

    public static JFrame showPanel(JPanel panel) {
        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.setResizable(true);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

    public static void getAllObjects(DefaultMutableTreeNode node, List l) {
        if (node.getUserObject() instanceof Obj) {
            Obj o = (Obj) node.getUserObject();
            if (o.userData != null) {
                l.add(o.userData);
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            getAllObjects((DefaultMutableTreeNode) node.getChildAt(i), l);
        }

    }

    public static DefaultMutableTreeNode generateNodes(List<String> paths, List objects) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        int i = 0;
        for (String path : paths) {

            DefaultMutableTreeNode current = root;
            String[] splits = path.split("/");
            int idx = 0;
            for (String name : splits) {
                Enumeration en = current.children();
                boolean found = false;
                Obj o = new Obj();
                o.name = name;
                o.userData = (idx == splits.length - 1) ? objects.get(i) : null;

                DefaultMutableTreeNode node = null;
                while (en.hasMoreElements()) {
                    if ((node = (DefaultMutableTreeNode) en.nextElement()).getUserObject().toString().equals(name)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    DefaultMutableTreeNode n = new DefaultMutableTreeNode(o);
                    node = n;
                    current.add(node);
                } else {
                    node.setUserObject(o);
                }
                idx++;
                current = node;
            }
            i++;

        }
        return root;
    }

    public static long generateID(List<Long> ids) {
        long rand;
        while (true) {
            rand = (long) (Math.random() * Long.MAX_VALUE);
            if (!ids.contains(rand)) {
                return rand;
            }
        }
    }

    public static byte[] toBytes(long f) {
        ByteBuffer b = ByteBuffer.allocate(Long.BYTES);
        b.putLong(f);
        return b.array();
    }

    public static byte[] toBytes(int f) {
        ByteBuffer b = ByteBuffer.allocate(Integer.BYTES);
        b.putInt(f);
        return b.array();
    }

    public static byte[] toBytes(float f) {
        ByteBuffer b = ByteBuffer.allocate(Float.BYTES);
        b.putFloat(f);
        return b.array();
    }

    public static byte[] toBytes(short f) {
        ByteBuffer b = ByteBuffer.allocate(Short.BYTES);
        b.putShort(f);
        return b.array();
    }

    public static byte[] toBytes(double f) {
        ByteBuffer b = ByteBuffer.allocate(Double.BYTES);
        b.putDouble(f);
        return b.array();
    }

    public static byte[] toBytes(boolean f) {
        ByteBuffer b = ByteBuffer.allocate(1);
        b.put((byte) (f ? 1 : 0));
        return b.array();
    }

    public static long getLong(byte[] bytes) {

        ByteBuffer b = ByteBuffer.wrap(bytes);
        return b.getLong();
    }

    public static int getInt(byte[] bytes) {

        ByteBuffer b = ByteBuffer.wrap(bytes);
        return b.getInt();
    }

    public static float getFloat(byte[] bytes) {

        ByteBuffer b = ByteBuffer.wrap(bytes);
        return b.getFloat();
    }

    public static double getDouble(byte[] bytes) {

        ByteBuffer b = ByteBuffer.wrap(bytes);
        return b.getDouble();
    }

    public static short getShort(byte[] bytes) {

        ByteBuffer b = ByteBuffer.wrap(bytes);
        return b.getShort();
    }

    public static boolean getBool(byte[] bytes) {

        ByteBuffer b = ByteBuffer.wrap(bytes);
        return b.get() == 1;
    }

    public static RandomAccessFile getFile(String mode, String... paths) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {

            builder.append(paths[i]);
            if (i != paths.length - 1 && !paths[i].endsWith("/")) {
                builder.append('/');
            }

        }

        String path = builder.toString();
        File f = new File(path);
        System.out.print(path);
        if (!f.exists()) {
            try {
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

        try {

            RandomAccessFile file = new RandomAccessFile(f, mode);
            return file;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static void showMsg(String msg) {

        final JFrame frame = new JFrame("Msg");
        Globals.Msg = new WeakReference<>(frame);

        Msg container = new Msg();
        container.setData(msg, frame);

        frame.getContentPane().add(container, BorderLayout.CENTER);

        Dimension d = new Dimension(1200, 800);
        frame.setSize(d);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
    }

    public static void showMsg(String msg, Runnable run) {

        final JFrame frame = new JFrame("Msg");
        Globals.Msg = new WeakReference<>(frame);

        Msg container = new Msg();
        container.setData(msg, frame);
        container.toggleContinue();
        container.setRunnable(run);
        frame.getContentPane().add(container, BorderLayout.CENTER);
        Dimension d = new Dimension(1200, 800);
        frame.setSize(d);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
    }

    public static boolean createDir(String... paths) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {

            builder.append(paths[i]);
            if (!paths[i].endsWith("/")) {
                builder.append('/');
            }

        }
        String path = builder.toString();
        File f = new File(path);
        boolean mkdirs = f.mkdirs();
        return mkdirs;

    }

}
