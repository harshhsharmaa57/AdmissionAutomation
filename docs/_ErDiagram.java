import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class _ErDiagram {
    static final Color NAVY = new Color(0x12, 0x3A, 0x63);
    static final Color ACCENT = new Color(0x9E, 0x1B, 0x1B);
    static final Color INK = new Color(0x22, 0x2A, 0x35);
    static final Color MUTED = new Color(0x5F, 0x6B, 0x7A);
    static final Color BORDER = new Color(0xB9, 0xC6, 0xD6);
    static final Color SOFT = new Color(0xF3, 0xF6, 0xFA);

    public static void main(String[] args) throws Exception {
        int W = 1200, H = 850;
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, W, H);

        drawCentered(g, "Entity Relationship Diagram", 30, Font.BOLD, NAVY, W / 2, 42);
        drawCentered(g, "ABES Engineering College  |  Admission Management Portal", 16, Font.PLAIN, MUTED, W / 2, 68);

        box(g, 20, 190, 300, 260, "USER", new String[]{
            "PK  _id",
            "name",
            "email  (unique)",
            "password  (bcrypt)",
            "phone",
            "role: student/reviewer/admin",
            "createdAt / updatedAt"
        }, false);
        box(g, 920, 190, 260, 300, "COURSE", new String[]{
            "PK  _id",
            "code  (unique)",
            "name",
            "degree · duration",
            "seats · feePerYear",
            "eligibility · description",
            "isActive",
            "createdAt / updatedAt"
        }, false);
        box(g, 20, 690, 300, 105, "NOTICE", new String[]{
            "title · body · category",
            "date · isActive · standalone"
        }, true);
        box(g, 920, 690, 260, 105, "COUNTER", new String[]{
            "_id · seq",
            "Atomic number generator"
        }, true);

        // Application entity
        int ax = 420, ay = 120, aw = 380, ah = 540;
        fillRound(g, ax, ay, aw, ah, NAVY);
        fillRound(g, ax, ay + 36, aw, ah - 36, Color.WHITE);
        g.setColor(NAVY);
        g.setStroke(new BasicStroke(2f));
        drawRound(g, ax, ay, aw, ah, 14);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("APPLICATION", ax + 16, ay + 25);
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        String[] top = {
            "PK  applicationNo  (unique)",
            "FK  applicant  →  User",
            "FK  course  →  Course",
            "session · status · remarks"
        };
        int ty = ay + 66;
        for (String s : top) {
            g.setColor(INK);
            g.drawString(s, ax + 16, ty);
            ty += 27;
        }

        // Embedded value objects
        int ex = ax + 12, ey = 300, ew = aw - 24, eh = 220;
        g.setColor(SOFT);
        g.fillRoundRect(ex, ey, ew, eh, 10, 10);
        g.setColor(BORDER);
        g.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{5f, 4f}, 0));
        g.drawRoundRect(ex, ey, ew, eh, 10, 10);
        g.setStroke(new BasicStroke(1f));
        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        g.setColor(ACCENT);
        g.drawString("EMBEDDED VALUE OBJECTS", ex + 12, ey + 20);
        g.setFont(new Font("SansSerif", Font.PLAIN, 17));
        g.setColor(INK);
        String[] embedded = {
            "personal (name, DOB, category)",
            "academics (Class 10, 12, exam)",
            "documents[] (photo, marksheets, ID)",
            "timeline[] (status, note, date)",
            "review (checklist, notes, reviewer)"
        };
        int eyy = ey + 46;
        for (String s : embedded) {
            g.drawString(s, ex + 12, eyy);
            eyy += 27;
        }

        g.setColor(MUTED);
        g.setFont(new Font("SansSerif", Font.ITALIC, 15));
        g.drawString("Unique index: applicant + course", ax + 16, 558);
        g.drawString("(one application per programme)", ax + 16, 580);

        // Relationship lines
        relationship(g, 320, 300, 420, 300, "1", "N", "submits");
        relationship(g, 920, 310, 800, 310, "1", "N", "receives");

        // Legend and note
        g.setColor(BORDER);
        g.setStroke(new BasicStroke(1f));
        g.drawLine(435, 685, 510, 685);
        g.setFont(new Font("SansSerif", Font.PLAIN, 15));
        g.setColor(MUTED);
        g.drawString("solid line = ObjectId reference", 520, 689);
        g.drawString("dashed box = data embedded in the application document", 420, 716);
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.setColor(MUTED);
        drawCentered(g, "MongoDB collections: users, courses, applications, notices, counters", 14, Font.PLAIN, MUTED, W / 2, 830);

        g.dispose();
        ImageIO.write(img, "png", new File("docs/_er-diagram.png"));
        System.out.println("Wrote docs/_er-diagram.png");
    }

    static void box(Graphics2D g, int x, int y, int w, int h, String title, String[] lines, boolean muted) {
        Color header = muted ? new Color(0x5F, 0x6B, 0x7A) : NAVY;
        fillRound(g, x, y, w, h, header);
        fillRound(g, x, y + 36, w, h - 36, Color.WHITE);
        g.setColor(header);
        g.setStroke(new BasicStroke(2f));
        drawRound(g, x, y, w, h, 14);
        g.setFont(new Font("SansSerif", Font.BOLD, 19));
        g.setColor(Color.WHITE);
        g.drawString(title, x + 14, y + 24);
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        int ly = y + 62;
        for (String s : lines) {
            g.setColor(INK);
            g.drawString(s, x + 14, ly);
            ly += 27;
        }
    }

    static void relationship(Graphics2D g, int x1, int y1, int x2, int y2, String leftCard, String rightCard, String label) {
        g.setColor(ACCENT);
        g.setStroke(new BasicStroke(2f));
        g.drawLine(x1, y1, x2, y2);
        int dir = x2 > x1 ? 1 : -1;
        Polygon arrow = new Polygon(
            new int[]{x2, x2 - dir * 11, x2 - dir * 11},
            new int[]{y2, y2 - 6, y2 + 6}, 3);
        g.fillPolygon(arrow);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.setColor(ACCENT);
        if (dir > 0) {
            g.drawString(leftCard, x1 + 12, y1 - 8);
            g.drawString(rightCard, x2 - 22, y2 - 8);
        } else {
            g.drawString(leftCard, x1 - 22, y1 - 8);
            g.drawString(rightCard, x2 + 12, y2 - 8);
        }
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.setColor(MUTED);
        int mid = (x1 + x2) / 2;
        int tw = g.getFontMetrics().stringWidth(label);
        g.drawString(label, mid - tw / 2, y1 + 19);
    }

    static void fillRound(Graphics2D g, int x, int y, int w, int h, Color c) {
        g.setColor(c);
        g.fill(new RoundRectangle2D.Double(x, y, w, h, 14, 14));
    }

    static void drawRound(Graphics2D g, int x, int y, int w, int h, int r) {
        g.draw(new RoundRectangle2D.Double(x, y, w, h, r, r));
    }

    static void drawCentered(Graphics2D g, String text, int size, int style, Color color, int cx, int y) {
        g.setFont(new Font("SansSerif", style, size));
        g.setColor(color);
        int tw = g.getFontMetrics().stringWidth(text);
        g.drawString(text, cx - tw / 2, y);
    }
}
