import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GameOfLife {
    static int gameSpeed = 100;
    static boolean gameRunning = false;
    static int tilesX = 80;
    static int tilesY = 60;
    static int tileSizeX = 12;
    static int tileSizeY = 12;
    static int margX = 32;
    static int margY = 16;
    static int marginText = 170;
    static int width = margX * 2 + tileSizeX * tilesX + marginText;
    static int height = margY * 2 + margX * 2 + tileSizeY * tilesY;

    // Different directions
    static Point[] directions = {new Point(-1, 0), new Point(0, -1),
                                 new Point(1, 0), new Point(0, 1),
                                new Point(1, 1), new Point(-1, -1),
                                new Point(-1, 1), new Point(1, -1)};

    // Shapes
    static ArrayList<ArrayList> shapes = new ArrayList<>();
    static int selected_shape = 0;
    // Map
    static boolean[][] tiles = new boolean[tilesX][tilesY];
    static Color colorOn = new Color(250, 230, 40);
    static Color colorPreview = new Color(180, 180, 180);
    static Color paperColor = new Color(230, 230, 230);

    static Point mPos = null;
    static Timer timer = null;
    public static void main(String[] args) {
        // Shapes
        shapes.add(new ArrayList<Point>());
        shapes.get(0).add (new Point (0,0)); // Pixel
        shapes.add(new ArrayList<Point>());
        shapes.get(1).add (new Point (0,0)); // Static Square
        shapes.get(1).add (new Point (1,0));
        shapes.get(1).add (new Point (0,1));
        shapes.get(1).add (new Point (1,1));
        shapes.add(new ArrayList<Point>());
        shapes.get(2).add (new Point (0,0)); // Glider
        shapes.get(2).add (new Point (2,0));
        shapes.get(2).add (new Point (1,1));
        shapes.get(2).add (new Point (2,1));
        shapes.get(2).add (new Point (1,2));
        shapes.add(new ArrayList<Point>());
        shapes.get(3).add (new Point (0,0)); // Glider LightWeight
        shapes.get(3).add (new Point (3,0));
        shapes.get(3).add (new Point (4,1));
        shapes.get(3).add (new Point (0,2));
        shapes.get(3).add (new Point (4,2));
        shapes.get(3).add (new Point (1,3));
        shapes.get(3).add (new Point (2,3));
        shapes.get(3).add (new Point (3,3));
        shapes.get(3).add (new Point (4,3));
        shapes.add(new ArrayList<Point>());
        shapes.get(4).add (new Point (0,4)); // Glider gun
        shapes.get(4).add (new Point (1,4));
        shapes.get(4).add (new Point (0,5));
        shapes.get(4).add (new Point (1,5));

        shapes.get(4).add (new Point (10,4));
        shapes.get(4).add (new Point (10,5));
        shapes.get(4).add (new Point (10,6));
        shapes.get(4).add (new Point (11,3));
        shapes.get(4).add (new Point (11,7));
        shapes.get(4).add (new Point (12,2));
        shapes.get(4).add (new Point (12,8));
        shapes.get(4).add (new Point (13,2));
        shapes.get(4).add (new Point (13,8));

        shapes.get(4).add (new Point (14,5));
        shapes.get(4).add (new Point (15,3));
        shapes.get(4).add (new Point (15,7));

        shapes.get(4).add (new Point (16,4));
        shapes.get(4).add (new Point (16,5));
        shapes.get(4).add (new Point (16,6));
        shapes.get(4).add (new Point (17,5));

        shapes.get(4).add (new Point (20,2)); // Box ship
        shapes.get(4).add (new Point (20,3));
        shapes.get(4).add (new Point (20,4));
        shapes.get(4).add (new Point (21,2));
        shapes.get(4).add (new Point (21,3));
        shapes.get(4).add (new Point (21,4));
        shapes.get(4).add (new Point (22,1));
        shapes.get(4).add (new Point (22,5));
        shapes.get(4).add (new Point (24,0));
        shapes.get(4).add (new Point (24,1));
        shapes.get(4).add (new Point (24,5));
        shapes.get(4).add (new Point (24,6));

        shapes.get(4).add (new Point (34,2));
        shapes.get(4).add (new Point (34,3));
        shapes.get(4).add (new Point (35,2));
        shapes.get(4).add (new Point (35,3));

        // Frames and panel
        JFrame frame = new JFrame("Conway's Game of Life");

        JPanel panel = new JPanel() {
            public void paint(Graphics g) {
                super.paint(g);

                // Bakgrunden
                for (int y = 0; y < tilesY; y++) {
                    for (int x = 0; x < tilesX; x++) {
                        if (tiles[x][y]) {
                            g.setColor(colorOn);
                        } else {
                            g.setColor(paperColor);
                        }
                        g.fillRect(margX + x * tileSizeX,
                                margY + y * tileSizeY, tileSizeX - 2, tileSizeY - 2);

                    }
                }

                // Mouse preview
                if (mPos != null) {
                    g.setColor(colorPreview);
                    for (Point p : (ArrayList<Point>) shapes.get(selected_shape)) {
                        g.fillRect(margX + (mPos.x + p.x) * tileSizeX,
                                margY + (mPos.y + p.y) * tileSizeY, tileSizeX - 2, tileSizeY - 2);
                    }
                }

                // Print text
                g.setColor (Color.BLACK);
                String outText;
                if (!gameRunning) {
                    outText = "Game Paused";
                    g.drawString(outText ,width - 170, margY + (g.getFontMetrics().getHeight() + 2) * 2);
                }
                outText = "delay: " + gameSpeed;
                g.drawString(outText ,width - 170, margY + (g.getFontMetrics().getHeight() + 2) * 3);
                g.setColor(colorOn);
                for (Point p: (ArrayList<Point>) shapes.get (selected_shape)) {
                    g.fillRect(width - 170 + p.x * tileSizeX,
                            margY + 100 + p.y * tileSizeY, tileSizeX - 2, tileSizeY - 2);
                }
            }
        };
        frame.setSize(width, height);
        frame.add(panel);
        frame.getContentPane().setBackground(paperColor);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        newGame();
        frame.setVisible(true);
        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mPos = new Point ((e.getX()-margX)/ tileSizeX, (e.getY()-margY)/ tileSizeY);
            }
        });
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mPos = null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int x = (e.getX() - margX) / tileSizeX;
                int y = (e.getY() - margY) / tileSizeY;
                if (selected_shape == 0) {
                    tiles[x][y] = !tiles[x][y];
                } else {
                    for (Point p: (ArrayList<Point>) shapes.get (selected_shape)) {
                        tiles[x+p.x][y+p.y] = true;
                    }
                }
                mPos = null;
            }
        });

        frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent keyEvent) {
                //System.out.println (keyEvent.getKeyCode ());
                if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                    gameRunning = ! gameRunning;
                }
                else if (keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    newGame();
                }
                else if (keyEvent.getKeyCode() >= KeyEvent.VK_0 && keyEvent.getKeyCode() <= KeyEvent.VK_9) {
                    selected_shape = keyEvent.getKeyCode() - KeyEvent.VK_0;
                    if (selected_shape >= shapes.size())
                        selected_shape = 0;
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_COMMA) {
                    gameSpeed += 10;
                    timer.setDelay(gameSpeed);
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_PERIOD) {
                    gameSpeed -= 10;
                    if (gameSpeed < 0) gameSpeed = 0;
                    timer.setDelay(gameSpeed);
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        ActionListener timeActionL = actionEvent -> {
            if (!gameRunning) {
                panel.repaint();
                return;
            }

            boolean[][] nextGen = new boolean[tilesX][tilesY];
            // Create next generation
            for (int y = 0; y < tilesY; y++) {
                for (int x = 0; x < tilesX; x++) {
                    int neighbours = 0;
                    for (int i = 0; i < 8; i++) {
                        int xx = x + directions[i].x;
                        int yy = y + directions[i].y;

                        if (xx >= 0 && yy >= 0 && xx < tilesX && yy < tilesY && tiles[xx][yy]) {
                            neighbours++;
                        }
                    }
                    if (tiles[x][y]) {
                        nextGen[x][y] = neighbours >= 2 && neighbours < 4;
                    } else {
                        if (neighbours == 3)
                            nextGen[x][y] = true; 
                    }
                } // for x
            } // for y

            // Swap
            tiles = nextGen;

            frame.repaint();
        };
        timer = new Timer(gameSpeed, timeActionL);

        timer.start();
    }

    private static void newGame() {
        // Clear map
        for (int y = 0; y < tilesY; y++)
            for (int x = 0; x < tilesX; x++) {
                tiles[x][y] = false;
            }

        gameRunning = false;
    }
    private static void copyMap(boolean[][] sourceMap, boolean[][] destMap) {
        // Copy m1 to m2
        if (sourceMap == null || destMap == null) return;
        for (int y = 0; y < tilesY; y++)
            for (int x = 0; x < tilesX; x++) {
                destMap[x][y] = sourceMap[x][y];
            }
    }
}
