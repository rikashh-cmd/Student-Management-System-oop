
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Student {
    private String studentId;
    private String name;
    private String nic;
    private String batchNumber;
    private String lecturerMode;
    private int prfMarks;
    private int dbmsMarks;

    public Student(String studentId, String name, String nic, String batchNumber, String lecturerMode) {
        this.studentId = studentId;
        this.name = name;
        this.nic = nic;
        this.batchNumber = batchNumber;
        this.lecturerMode = lecturerMode;
        this.prfMarks = -2;  
        this.dbmsMarks = -2; 
    }

    public Student(String studentId, String nic, String name, int prfMarks, int dbmsMarks) {
        this.studentId = studentId;
        this.nic = nic;
        this.name = name;
        this.prfMarks = prfMarks;
        this.dbmsMarks = dbmsMarks;
        this.batchNumber = (studentId.length() >= 7 ? studentId.substring(4, 7) : "105");
        this.lecturerMode = studentId.startsWith("OR") ? "ONLINE" : "PHYSICAL";
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
    public String getBatchNumber() { return batchNumber; }
    public String getLecturerMode() { return lecturerMode; }
    public int getPrfMarks() { return prfMarks; }
    public void setPrfMarks(int prfMarks) { this.prfMarks = prfMarks; }
    public int getDbmsMarks() { return dbmsMarks; }
    public void setDbmsMarks(int dbmsMarks) { this.dbmsMarks = dbmsMarks; }

    public static double getGpaForMark(int mark) {
        if (mark < 0)   return 0.0;
        if (mark >= 90) return 4.25; 
        if (mark >= 80) return 4.00;
        if (mark >= 75) return 3.70;
        if (mark >= 70) return 3.30;
        if (mark >= 65) return 3.00;
        if (mark >= 60) return 2.70;
        if (mark >= 55) return 2.30;
        if (mark >= 50) return 2.00;
        if (mark >= 45) return 1.70;
        if (mark >= 40) return 1.30;
        if (mark >= 30) return 1.00;
        if (mark >= 20) return 0.70;
        return 0.00;
    }

    public static int[] batchNameArray = { 105, 106, 107, 108, 109, 110 };
    public static int[] batchStatusArray = { 0, 0, 0, 0, 1, 1 };

    public double calculateGpa() {
        if (prfMarks < 0 && dbmsMarks < 0) return 0.0;
        double gpa1 = getGpaForMark(prfMarks);
        double gpa2 = getGpaForMark(dbmsMarks);
        return (gpa1 + gpa2) / 2.0;
    }

    public int getTotalMarks() {
        int m1 = prfMarks > 0 ? prfMarks : 0;
        int m2 = dbmsMarks > 0 ? dbmsMarks : 0;
        return m1 + m2;
    }
}

class BatchRepository {
    public static List<Integer> batchNumbers = new ArrayList<>();
    public static List<Integer> batchStatuses = new ArrayList<>();

    static {
        for (int b : Student.batchNameArray) {
            batchNumbers.add(b);
        }
        for (int s : Student.batchStatusArray) {
            batchStatuses.add(s);
        }
    }

    public static boolean exists(int batchNo) {
        return batchNumbers.contains(batchNo);
    }

    public static int getStatus(int batchNo) {
        int index = batchNumbers.indexOf(batchNo);
        return index != -1 ? batchStatuses.get(index) : 0;
    }

    public static void updateStatus(int batchNo, int newStatus) {
        int index = batchNumbers.indexOf(batchNo);
        if (index != -1) {
            batchStatuses.set(index, newStatus);
        }
    }

    public static void addBatch(int batchNo) {
        batchNumbers.add(batchNo);
        batchStatuses.add(1); // Default ENROLLMENT OPEN
    }

    public static int getStudentCountForBatch(int batchNo) {
        int count = 0;
        String bStr = String.valueOf(batchNo);
        for (Student s : StudentRepository.getAllStudents()) {
            if (s.getBatchNumber().equals(bStr)) {
                count++;
            }
        }
        return count;
    }
}

class StudentRepository {
    private static final List<Student> studentList = new ArrayList<>();
    private static int studentCounter = 89;

    public static Student[] studentArray = {
        new Student("PR24105001", "199501012345", "Gunawardena Weerasinghe", 85, 66),
        new Student("PR24105002", "199503153872", "Senanayake Silva", 39, 45),
        new Student("PR24105003", "199506202198", "Silva Kumara", -1, 93),
        new Student("PR24105004", "199509102983", "Kumara Herath", 72, 58),
        new Student("PR24105005", "199511258739", "Rathnayake Herath", 44, -1),
        new Student("PR24105006", "199512303498", "Wijesinghe Bandara", 91, 37),
        new Student("PR24105007", "199502183764", "Rajapaksha Herath", 60, 88),
        new Student("PR24105008", "199504223198", "Senanayake Karunaratne", 38, 21),
        new Student("PR24105009", "199508153210", "Karunaratne Jayasinghe", 95, 79),
        new Student("PR24105010", "199510293417", "Gunawardena Silva", 49, 40),
        new Student("OR24105011", "199601102375", "Weerasinghe Rajapaksha", -1, 76),
        new Student("OR24105012", "199604182938", "Silva Rathnayake", 67, 54),
        new Student("OR24105013", "199606243879", "Fernando Perera", 23, -1),
        new Student("OR24105014", "199608142178", "Kumara Abeysekera", 58, 69),
        new Student("OR24105015", "199610312475", "Ekanayake Rathnayake", 88, 92),
        new Student("PR24105016", "199611173452", "Herath Gunawardena", 81, 25),
        new Student("PR24105017", "199603293481", "Abeysekera Silva", 73, 84),
        new Student("PR24105018", "199605083217", "Weerasinghe Silva", 29, 33),
        new Student("OR24105019", "199607232198", "Jayasinghe Dias", 62, 60),
        new Student("OR24105020", "199609192375", "Bandara Rathnayake", -1, 71),
        new Student("PR24105021", "199701212483", "Silva Perera", 79, 59),
        new Student("PR24105022", "199703132487", "De Silva Dias", 53, -1),
        new Student("OR24105023", "199706253478", "Abeysekera Jayasinghe", 94, 98),
        new Student("OR24105024", "199708083298", "Rajapaksha Senanayake", 47, 27),
        new Student("PR24105025", "199710243651", "Kumara Karunaratne", 35, 48),
        new Student("PR24106001", "199712152983", "Silva Abeysekera", 93, 35),
        new Student("PR24106002", "199702182734", "Jayasinghe Bandara", 15, 91),
        new Student("PR24106003", "199704293187", "Rathnayake Kumara", -1, 60),
        new Student("PR24106004", "199705142375", "Weerasinghe Rajapaksha", 82, -1),
        new Student("PR24106005", "199709083751", "Senanayake Herath", 45, 72),
        new Student("PR24106006", "199801032874", "Perera Ekanayake", 88, 49),
        new Student("PR24106007", "199803232871", "Herath Jayasinghe", 23, 26),
        new Student("PR24106008", "199806193428", "Kumara Gunawardena", 79, 80),
        new Student("PR24106009", "199808013764", "Abeysekera Silva", 37, 14),
        new Student("PR24106010", "199810242374", "Dias Fernando", -1, 89),
        new Student("OR24106011", "199812302984", "Karunaratne Weerasinghe", 68, 67),
        new Student("OR24106012", "199802152348", "Ekanayake Bandara", 100, -1),
        new Student("OR24106013", "199805213471", "Rajapaksha Kumara", 59, 31),
        new Student("OR24106014", "199807172398", "Silva De Silva", 29, 94),
        new Student("OR24106015", "199811283472", "Gunawardena Rathnayake", 92, 53),
        new Student("PR24106016", "199901122471", "Bandara Karunaratne", 12, 78),
        new Student("PR24106017", "199903052984", "Fernando Perera", 77, 5),
        new Student("PR24106018", "199906213874", "De Silva Silva", 38, 90),
        new Student("OR24106019", "199908093412", "Rajapaksha Gunawardena", 66, 24),
        new Student("OR24106020", "199910273894", "Herath Weerasinghe", 9, 86),
        new Student("PR24106021", "199912153482", "Karunaratne Dias", 84, 39),
        new Student("PR24106022", "199902202394", "Jayasinghe Silva", 51, -1),
        new Student("OR24106023", "199904163874", "Senanayake Abeysekera", 32, 61),
        new Student("OR24106024", "199907293481", "Silva Jayasinghe", -1, 73),
        new Student("PR24106025", "199911083479", "Rathnayake Kumara", 97, 100),
        new Student("PR24107001", "200001112374", "Gunawardena Kumara", 95, 38),
        new Student("PR24107002", "200003143478", "Rajapaksha Silva", -1, 91),
        new Student("PR24107003", "200006293874", "Perera Jayasinghe", 63, -1),
        new Student("PR24107004", "200008103471", "Silva Ekanayake", 88, 74),
        new Student("PR24107005", "200010252984", "Dias Senanayake", 32, 55),
        new Student("PR24107006", "200012043894", "Herath Abeysekera", 76, 82),
        new Student("PR24107007", "200002193874", "Rathnayake Fernando", 97, 66),
        new Student("PR24107008", "200004212374", "Kumara Herath", 54, 49),
        new Student("PR24107009", "200005183492", "Weerasinghe Silva", -1, 99),
        new Student("PR24107010", "200007153871", "Senanayake Karunaratne", 23, 13),
        new Student("OR24107011", "200101232984", "Abeysekera Silva", 90, 80),
        new Student("OR24107012", "200103083471", "Bandara Gunawardena", 35, 70),
        new Student("OR24107013", "200106273894", "Karunaratne Weerasinghe", 81, 93),
        new Student("OR24107014", "200108123984", "Perera Herath", 61, 36),
        new Student("OR24107015", "200110043728", "Fernando Dias", 44, 59),
        new Student("PR24107016", "200112213874", "Weerasinghe Gunawardena", 67, 85),
        new Student("PR24107017", "200102253471", "Rathnayake Kumara", 100, 47),
        new Student("PR24107018", "200104103874", "Senanayake Fernando", 17, 90),
        new Student("OR24107019", "200105293784", "Silva Bandara", 85, -1),
        new Student("OR24107020", "200107202983", "Herath Rajapaksha", 29, 22),
        new Student("PR24107021", "200201013874", "Kumara Jayasinghe", 70, 77),
        new Student("PR24107022", "200203253471", "Abeysekera Perera", 42, 34),
        new Student("OR24107023", "200206143874", "Rathnayake Jayasinghe", -1, 63),
        new Student("OR24107024", "200208083471", "Kumara Weerasinghe", 60, 100),
        new Student("PR24107025", "200210293874", "Rajapaksha Ekanayake", 86, 29),
        new Student("PR24108001", "200212183471", "Fernando Rajapaksha", 86, 79),
        new Student("PR24108002", "200202103874", "Silva Gunawardena", 57, 62),
        new Student("PR24108003", "200204123894", "Perera Wijesinghe", 91, 87),
        new Student("PR24108004", "200205283471", "Herath Abeysekera", 35, -1),
        new Student("PR24108005", "200207153874", "Rajapaksha Ekanayake", -1, 54),
        new Student("PR24108006", "200301093874", "Karunaratne Silva", 76, 46),
        new Student("PR24108007", "200303283471", "Weerasinghe Fernando", 48, 99),
        new Student("PR24108008", "200306153874", "Silva Bandara", 94, 39),
        new Student("PR24108009", "200308123471", "Abeysekera Weerasinghe", 23, 70),
        new Student("PR24108010", "200310083874", "Kumara Karunaratne", 69, -1),
        new Student("OR24108011", "200312243471", "Dias Rajapaksha", -1, 75),
        new Student("OR24108012", "200302273874", "Herath Perera", 80, 83),
        new Student("OR24108013", "200304203471", "Rathnayake Gunawardena", 55, 58),
        new Student("OR24108014", "200305123874", "Ekanayake Jayasinghe", 88, 92),
        new Student("OR24108015", "200307213471", "Gunawardena Silva", 32, 30),
        new Student("PR24108016", "200401153874", "Rajapaksha Perera", 100, 91),
        new Student("PR24108017", "200403123471", "Karunaratne Jayasinghe", 67, 40),
        new Student("PR24108018", "200406293874", "Weerasinghe Abeysekera", 43, 63),
        new Student("OR24108019", "200408083471", "Rathnayake Fernando", -1, 95),
        new Student("OR24108020", "200410213874", "Kumara Herath", 90, 68),
        new Student("PR24108021", "200412153471", "Silva Weerasinghe", 60, -1),
        new Student("PR24108022", "200402203874", "Herath Karunaratne", 77, 66),
        new Student("OR24108023", "200404273471", "Abeysekera Silva", 25, 21),
        new Student("OR24108024", "200405143874", "Gunawardena Ekanayake", 71, 88),
        new Student("PR24108025", "200407183471", "Weerasinghe Kumara", 84, 37),
        new Student("PR24109001", "200501023874", "Weerasinghe Kumara", 92, 67),
        new Student("PR24109002", "200503193471", "Rajapaksha Abeysekera", 68, 91),
        new Student("PR24109003", "200506153874", "Gunawardena Perera", 59, 85),
        new Student("PR24109004", "200508213471", "Karunaratne Silva", 85, 73),
        new Student("PR24109005", "200510083874", "Herath Wijesinghe", 63, 70),
        new Student("PR24109006", "200512293471", "Rathnayake Ekanayake", 76, 63),
        new Student("PR24109007", "200502123874", "Silva Fernando", 91, 76),
        new Student("PR24109008", "200504153471", "Abeysekera Rajapaksha", 70, 88),
        new Student("PR24109009", "200505283874", "Fernando Bandara", 84, 55),
        new Student("PR24109010", "200507173471", "Perera Herath", 63, 64),
        new Student("OR24109011", "200203456782", "Weerasinghe Jayasinghe", 72, 79),
        new Student("OR24109012", "200305678901", "Silva Karunaratne", 89, 80),
        new Student("OR24109013", "199601234567", "Rathnayake Gunawardena", 45, 59),
        new Student("OR24109014", "199511223344", "Herath Kumara", 81, 92),
        new Student("OR24109015", "200412345678", "Abeysekera Silva", 77, 68),
        new Student("PR24109016", "200512345678", "Ekanayake Bandara", 68, 100),
        new Student("PR24109017", "199909876543", "Rajapaksha Fernando", 63, 77),
        new Student("PR24109018", "199812346789", "Gunawardena Weerasinghe", 88, 83),
        new Student("OR24109019", "200010203040", "Kumara Karunaratne", 75, 45),
        new Student("OR24109020", "200608789012", "Silva Dias", 90, 62),
        new Student("PR24109021", "200012345678", "Perera Weerasinghe", 57, 66),
        new Student("PR24109022", "199812345679", "Karunaratne Rajapaksha", 79, 59),
        new Student("OR24109023", "199902345678", "Jayasinghe Silva", 92, 78),
        new Student("OR24109024", "199712345670", "Rathnayake Perera", 62, 85),
        new Student("PR24109025", "200102345671", "Silva Ekanayake", 100, 56),
        new Student("PR24110001", "200203456782", "Silva Karunaratne", -2, -2),
        new Student("PR24110002", "200305678901", "Herath Fernando", -2, -2),
        new Student("PR24110003", "199601234567", "Kumara Jayasinghe", -2, -2),
        new Student("PR24110004", "199511223344", "Weerasinghe Perera", -2, -2),
        new Student("PR24110005", "200412345678", "Abeysekera Rajapaksha", -2, -2),
        new Student("PR24110006", "200512345678", "Rathnayake Karunaratne", -2, -2),
        new Student("PR24110007", "199909876543", "Ekanayake Bandara", -2, -2),
        new Student("PR24110008", "199812346789", "Gunawardena Perera", -2, -2),
        new Student("PR24110009", "200010203040", "Silva Wijesinghe", -2, -2),
        new Student("PR24110010", "200608789012", "Rajapaksha Jayasinghe", -2, -2),
        new Student("OR24110011", "200012345678", "Rathnayake Fernando", -2, -2),
        new Student("OR24110012", "199812345679", "Karunaratne Kumara", -2, -2),
        new Student("OR24110013", "199902345678", "Perera Silva", -2, -2),
        new Student("OR24110014", "199712345670", "Gunawardena Ekanayake", -2, -2),
        new Student("OR24110015", "200102345671", "Bandara Rajapaksha", -2, -2),
        new Student("PR24110016", "200203456782", "Silva Herath", -2, -2),
        new Student("PR24110017", "200305678901", "Rathnayake Weerasinghe", -2, -2),
        new Student("PR24110018", "199601234567", "Perera Gunawardena", -2, -2),
        new Student("OR24110019", "199511223344", "Herath Karunaratne", -2, -2),
        new Student("OR24110020", "200412345678", "Silva Rajapaksha", -2, -2),
        new Student("PR24110021", "200203456782", "Ekanayake Kumara", -2, -2),
        new Student("PR24110022", "200305678901", "Bandara Herath", -2, -2),
        new Student("OR24110023", "199601234567", "Weerasinghe Rajapaksha", -2, -2),
        new Student("OR24110024", "199511223344", "Karunaratne Abeysekera", -2, -2),
        new Student("PR24110025", "200412345678", "Perera Dias", -2, -2)
    };

    static {
        for (Student s : studentArray) {
            studentList.add(s);
        }
    }

    public static List<Student> getAllStudents() {
        return studentList;
    }

    public static boolean isNicExists(String nic) {
        for (Student s : studentList) {
            if (s.getNic().equalsIgnoreCase(nic.trim())) {
                return true;
            }
        }
        return false;
    }

    public static Student findById(String id) {
        for (Student s : studentList) {
            if (s.getStudentId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    public static String generateNextId(String lecturerMode) {
        String prefix = lecturerMode.equalsIgnoreCase("ONLINE") ? "OR" : "PR";
        return String.format("%s2411%04d", prefix, studentCounter++);
    }

    public static void addStudent(Student student) {
        studentList.add(student);
    }

    public static boolean deleteStudent(String id) {
        Student s = findById(id);
        if (s != null) {
            return studentList.remove(s);
        }
        return false;
    }
}

public class Sms extends JFrame {
    public Sms() {
        setTitle("iCET Learning Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left Panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        JPanel textGroup = new JPanel();
        textGroup.setBackground(Color.WHITE);
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));

        JLabel titlePart1 = new JLabel("iCET");
        titlePart1.setFont(new Font("SansSerif", Font.BOLD, 42));
        titlePart1.setForeground(new Color(15, 23, 42));
        titlePart1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart2 = new JLabel("Learning");
        titlePart2.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart2.setForeground(new Color(30, 41, 59));
        titlePart2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart3 = new JLabel("Management System");
        titlePart3.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart3.setForeground(new Color(30, 41, 59));
        titlePart3.setAlignmentX(Component.CENTER_ALIGNMENT);

        textGroup.add(titlePart1);
        textGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        textGroup.add(titlePart2);
        textGroup.add(titlePart3);
        leftPanel.add(textGroup);

        // Right Panel
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(24, 38, 115));

        JPanel menuGroup = new JPanel();
        menuGroup.setOpaque(false);
        menuGroup.setLayout(new BoxLayout(menuGroup, BoxLayout.Y_AXIS));

        JLabel mainHeading = new JLabel("iCET");
        mainHeading.setFont(new Font("SansSerif", Font.BOLD, 28));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeading1 = new JLabel("Learning Management System");
        subHeading1.setFont(new Font("SansSerif", Font.BOLD, 22));
        subHeading1.setForeground(Color.WHITE);
        subHeading1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeading2 = new JLabel("Choose an option from the menu below to get started");
        subHeading2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subHeading2.setForeground(new Color(203, 213, 225));
        subHeading2.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuGroup.add(mainHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        menuGroup.add(subHeading1);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 8)));
        menuGroup.add(subHeading2);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnStudentMgmt = createMenuButton("Student Management");
        JButton btnBatchMgmt = createMenuButton("Batch Management");
        JButton btnGradeMgmt = createMenuButton("Grade Management");
        JButton btnReportGen = createMenuButton("Report Generator");

        menuGroup.add(btnStudentMgmt);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        menuGroup.add(btnBatchMgmt);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        menuGroup.add(btnGradeMgmt);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        menuGroup.add(btnReportGen);

        rightPanel.add(menuGroup);

        add(leftPanel);
        add(rightPanel);

        btnStudentMgmt.addActionListener(e -> {
            new StudentManagementFrame(this).setVisible(true);
            setVisible(false);
        });

        btnBatchMgmt.addActionListener(e -> {
            new BatchManagementFrame(this).setVisible(true);
            setVisible(false);
        });

        btnGradeMgmt.addActionListener(e -> {
            new GradeManagementFrame(this).setVisible(true);
            setVisible(false);
        });

        btnReportGen.addActionListener(e -> {
            new ReportManagementFrame(this).setVisible(true);
            setVisible(false);
        });
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(new Color(15, 23, 42));
        button.setBackground(new Color(219, 234, 254));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(280, 42));
        button.setPreferredSize(new Dimension(280, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Sms().setVisible(true));
    }
}

class StudentManagementFrame extends JFrame {
    private final JFrame homeFrame;

    public StudentManagementFrame(JFrame homeFrame) {
        this.homeFrame = homeFrame;

        setTitle("Student Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left Panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        JPanel textGroup = new JPanel();
        textGroup.setBackground(Color.WHITE);
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));

        JLabel titlePart1 = new JLabel("iCET");
        titlePart1.setFont(new Font("SansSerif", Font.BOLD, 42));
        titlePart1.setForeground(new Color(15, 23, 42));
        titlePart1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart2 = new JLabel("Learning");
        titlePart2.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart2.setForeground(new Color(30, 41, 59));
        titlePart2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart3 = new JLabel("Management System");
        titlePart3.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart3.setForeground(new Color(30, 41, 59));
        titlePart3.setAlignmentX(Component.CENTER_ALIGNMENT);

        textGroup.add(titlePart1);
        textGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        textGroup.add(titlePart2);
        textGroup.add(titlePart3);

        leftPanel.add(textGroup);

        // Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(24, 38, 115));

        JPanel menuGroup = new JPanel();
        menuGroup.setOpaque(false);
        menuGroup.setLayout(new BoxLayout(menuGroup, BoxLayout.Y_AXIS));

        JLabel mainHeading = new JLabel("Student Management");
        mainHeading.setFont(new Font("SansSerif", Font.BOLD, 28));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeading = new JLabel("Choose an option from the menu below to get started");
        subHeading.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subHeading.setForeground(new Color(203, 213, 225));
        subHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuGroup.add(Box.createRigidArea(new Dimension(0, 30)));
        menuGroup.add(mainHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 8)));
        menuGroup.add(subHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton btnAddStudent = createMenuButton("Add Student");
        JButton btnUpdateStudent = createMenuButton("Update Student");
        JButton btnViewStudent = createMenuButton("View Student Profile");
        JButton btnDeleteStudent = createMenuButton("Delete Student Profile");

        menuGroup.add(btnAddStudent);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 12)));
        menuGroup.add(btnUpdateStudent);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 12)));
        menuGroup.add(btnViewStudent);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 12)));
        menuGroup.add(btnDeleteStudent);

        // Bottom Navigation
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        bottomPanel.setOpaque(false);

        JButton btnBack = new JButton("<- Back to Home Page");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnBack.setForeground(Color.WHITE);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            homeFrame.setVisible(true);
            dispose();
        });

        bottomPanel.add(btnBack);

        rightPanel.add(menuGroup, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(leftPanel);
        add(rightPanel);

        btnAddStudent.addActionListener(e -> {
            new AddStudentFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });

        btnUpdateStudent.addActionListener(e -> {
            new UpdateStudentFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });

        btnViewStudent.addActionListener(e -> {
            new ViewStudentFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });

        btnDeleteStudent.addActionListener(e -> {
            new DeleteStudentFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(new Color(15, 23, 42));
        button.setBackground(new Color(219, 234, 254));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(280, 40));
        button.setPreferredSize(new Dimension(280, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}

class AddStudentFrame extends JFrame {
    private final JFrame studentMgmtFrame;
    private final JFrame mainHomeFrame;
    private JTextField txtBatchNum, txtNic, txtName, txtLecturerMode;

    public AddStudentFrame(JFrame studentMgmtFrame, JFrame mainHomeFrame) {
        this.studentMgmtFrame = studentMgmtFrame;
        this.mainHomeFrame = mainHomeFrame;

        setTitle("Add Student");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Add Student");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setLayout(null);

        JLabel lblBatch = new JLabel("Batch Number");
        lblBatch.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblBatch.setBounds(50, 40, 140, 30);
        contentPanel.add(lblBatch);

        txtBatchNum = new JTextField();
        txtBatchNum.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBatchNum.setBounds(200, 40, 230, 32);
        contentPanel.add(txtBatchNum);

        JLabel lblNic = new JLabel("NIC");
        lblNic.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNic.setBounds(50, 95, 140, 30);
        contentPanel.add(lblNic);

        txtNic = new JTextField();
        txtNic.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtNic.setBounds(200, 95, 230, 32);
        contentPanel.add(txtNic);

        JLabel lblName = new JLabel("Name");
        lblName.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblName.setBounds(50, 150, 140, 30);
        contentPanel.add(lblName);

        txtName = new JTextField();
        txtName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtName.setBounds(200, 150, 420, 32);
        contentPanel.add(txtName);

        JLabel lblLecturer = new JLabel("Lecturer Mode");
        lblLecturer.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblLecturer.setBounds(50, 205, 140, 30);
        contentPanel.add(lblLecturer);

        txtLecturerMode = new JTextField();
        txtLecturerMode.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtLecturerMode.setBounds(200, 205, 230, 32);
        contentPanel.add(txtLecturerMode);

        JButton btnCancel = new JButton("CANCEL");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancel.setBackground(new Color(24, 38, 115));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBounds(310, 330, 180, 38);
        contentPanel.add(btnCancel);

        JButton btnAdd = new JButton("ADD STUDENT");
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAdd.setBackground(new Color(24, 38, 115));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setBounds(520, 330, 210, 38);
        contentPanel.add(btnAdd);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> {
            studentMgmtFrame.setVisible(true);
            dispose();
        });

        btnAdd.addActionListener(e -> {
            String batch = txtBatchNum.getText().trim();
            String nic = txtNic.getText().trim();
            String name = txtName.getText().trim();
            String mode = txtLecturerMode.getText().trim();

            if (batch.isEmpty() || nic.isEmpty() || name.isEmpty() || mode.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int bNo = Integer.parseInt(batch.replaceAll("[^0-9]", ""));
                if (BatchRepository.exists(bNo) && BatchRepository.getStatus(bNo) == 0) {
                    JOptionPane.showMessageDialog(this, "Student can not be added to this batch because enrollment is closed!", "ERROR", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (Exception ex) {}

            if (StudentRepository.isNicExists(nic)) {
                JOptionPane.showMessageDialog(this, "This student is already added to the system!", "ERROR", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String generatedRegNo = StudentRepository.generateNextId(mode);
            Student newStudent = new Student(generatedRegNo, name, nic, batch, mode);
            StudentRepository.addStudent(newStudent);

            new AddSuccessFrame(name, generatedRegNo, studentMgmtFrame, mainHomeFrame).setVisible(true);
            dispose();
        });
    }
}

class AddSuccessFrame extends JFrame {
    public AddSuccessFrame(String name, String regNo, JFrame studentMgmtFrame, JFrame mainHomeFrame) {
        setTitle("Add Student Successfully");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(24, 38, 115));
        mainPanel.setLayout(null);

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBounds(25, 15, 100, 30);
        mainPanel.add(logoLabel);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(242, 246, 255));
        cardPanel.setBounds(80, 130, 625, 150);

        JPanel textHolder = new JPanel();
        textHolder.setOpaque(false);
        textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.Y_AXIS));

        JLabel successMsg = new JLabel(name + " Successfully Added!");
        successMsg.setFont(new Font("SansSerif", Font.BOLD, 20));
        successMsg.setForeground(Color.BLACK);
        successMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel regMsg = new JLabel("Registration No : " + regNo);
        regMsg.setFont(new Font("SansSerif", Font.BOLD, 18));
        regMsg.setForeground(Color.BLACK);
        regMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

        textHolder.add(successMsg);
        textHolder.add(Box.createRigidArea(new Dimension(0, 15)));
        textHolder.add(regMsg);

        cardPanel.add(textHolder);
        mainPanel.add(cardPanel);

        JButton btnHome = new JButton("BACK TO HOME PAGE");
        btnHome.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnHome.setForeground(Color.WHITE);
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setBounds(25, 395, 220, 35);

        btnHome.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        JButton btnAddNew = new JButton("ADD NEW STUDENT");
        btnAddNew.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAddNew.setForeground(Color.WHITE);
        btnAddNew.setContentAreaFilled(false);
        btnAddNew.setBorderPainted(false);
        btnAddNew.setFocusPainted(false);
        btnAddNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddNew.setBounds(535, 395, 210, 35);

        btnAddNew.addActionListener(e -> {
            new AddStudentFrame(studentMgmtFrame, mainHomeFrame).setVisible(true);
            dispose();
        });

        mainPanel.add(btnHome);
        mainPanel.add(btnAddNew);

        add(mainPanel, BorderLayout.CENTER);
    }
}

class UpdateStudentFrame extends JFrame {
    private final JFrame studentMgmtFrame;
    private final JFrame mainHomeFrame;
    private JTextField txtSearchId, txtNic, txtName;
    private JLabel lblNic, lblName;
    private JButton btnUpdate, btnCancel;
    private Student currentStudent;

    public UpdateStudentFrame(JFrame studentMgmtFrame, JFrame mainHomeFrame) {
        this.studentMgmtFrame = studentMgmtFrame;
        this.mainHomeFrame = mainHomeFrame;

        setTitle("Update Student");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Update Student");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setLayout(null);

        txtSearchId = new JTextField(" Enter Student Registration No");
        txtSearchId.setFont(new Font("SansSerif", Font.ITALIC, 14));
        txtSearchId.setForeground(Color.GRAY);
        txtSearchId.setBounds(130, 40, 350, 35);

        txtSearchId.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (txtSearchId.getText().equals(" Enter Student Registration No")) {
                    txtSearchId.setText("");
                    txtSearchId.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    txtSearchId.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (txtSearchId.getText().trim().isEmpty()) {
                    txtSearchId.setFont(new Font("SansSerif", Font.ITALIC, 14));
                    txtSearchId.setForeground(Color.GRAY);
                    txtSearchId.setText(" Enter Student Registration No");
                }
            }
        });
        contentPanel.add(txtSearchId);

        JButton btnSearch = new JButton("SEARCH");
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSearch.setBackground(new Color(24, 38, 115));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setBounds(500, 40, 180, 35);
        contentPanel.add(btnSearch);

        lblNic = new JLabel("NIC");
        lblNic.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNic.setBounds(80, 130, 100, 30);
        lblNic.setVisible(false);
        contentPanel.add(lblNic);

        txtNic = new JTextField();
        txtNic.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtNic.setBounds(190, 130, 250, 32);
        txtNic.setVisible(false);
        contentPanel.add(txtNic);

        lblName = new JLabel("Name");
        lblName.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblName.setBounds(80, 190, 100, 30);
        lblName.setVisible(false);
        contentPanel.add(lblName);

        txtName = new JTextField();
        txtName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtName.setBounds(190, 190, 420, 32);
        txtName.setVisible(false);
        contentPanel.add(txtName);

        btnCancel = new JButton("CANCEL");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancel.setBackground(new Color(24, 38, 115));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBounds(310, 330, 180, 38);
        btnCancel.setVisible(false);
        contentPanel.add(btnCancel);

        btnUpdate = new JButton("UPDATE");
        btnUpdate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnUpdate.setBackground(new Color(24, 38, 115));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setBounds(520, 330, 210, 38);
        btnUpdate.setVisible(false);
        contentPanel.add(btnUpdate);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> {
            studentMgmtFrame.setVisible(true);
            dispose();
        });

        btnSearch.addActionListener(e -> {
            String searchId = txtSearchId.getText().trim();
            currentStudent = StudentRepository.findById(searchId);

            if (currentStudent == null) {
                JOptionPane.showMessageDialog(this, "This student not exist in the system", "ERROR", JOptionPane.ERROR_MESSAGE);
                lblNic.setVisible(false);
                txtNic.setVisible(false);
                lblName.setVisible(false);
                txtName.setVisible(false);
                btnCancel.setVisible(false);
                btnUpdate.setVisible(false);
            } else {
                txtNic.setText(currentStudent.getNic());
                txtName.setText(currentStudent.getName());
                
                lblNic.setVisible(true);
                txtNic.setVisible(true);
                lblName.setVisible(true);
                txtName.setVisible(true);
                btnCancel.setVisible(true);
                btnUpdate.setVisible(true);
            }
        });

        btnUpdate.addActionListener(e -> {
            if (currentStudent == null) return;

            String newNic = txtNic.getText().trim();
            String newName = txtName.getText().trim();

            if (newNic.isEmpty() || newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newNic.equalsIgnoreCase(currentStudent.getNic()) && StudentRepository.isNicExists(newNic)) {
                JOptionPane.showMessageDialog(this, "The entered NIC already exists in the system!", "ERROR", JOptionPane.WARNING_MESSAGE);
                return;
            }

            currentStudent.setNic(newNic);
            currentStudent.setName(newName);

            new UpdateSuccessFrame(currentStudent.getStudentId(), studentMgmtFrame, mainHomeFrame).setVisible(true);
            dispose();
        });
    }
}

class UpdateSuccessFrame extends JFrame {
    public UpdateSuccessFrame(String regNo, JFrame studentMgmtFrame, JFrame mainHomeFrame) {
        setTitle("Update Student Successfully");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(24, 38, 115));
        mainPanel.setLayout(null);

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBounds(25, 15, 100, 30);
        mainPanel.add(logoLabel);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(242, 246, 255));
        cardPanel.setBounds(80, 140, 625, 130);

        JLabel successMsg = new JLabel(regNo + " Details Successfully Updated!");
        successMsg.setFont(new Font("SansSerif", Font.BOLD, 20));
        successMsg.setForeground(Color.BLACK);

        cardPanel.add(successMsg);
        mainPanel.add(cardPanel);

        JButton btnHome = new JButton("BACK TO HOME PAGE");
        btnHome.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnHome.setForeground(Color.WHITE);
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setBounds(25, 395, 220, 35);

        btnHome.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        JButton btnUpdateNew = new JButton("UPDATE NEW STUDENT");
        btnUpdateNew.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnUpdateNew.setForeground(Color.WHITE);
        btnUpdateNew.setContentAreaFilled(false);
        btnUpdateNew.setBorderPainted(false);
        btnUpdateNew.setFocusPainted(false);
        btnUpdateNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdateNew.setBounds(515, 395, 230, 35);

        btnUpdateNew.addActionListener(e -> {
            new UpdateStudentFrame(studentMgmtFrame, mainHomeFrame).setVisible(true);
            dispose();
        });

        mainPanel.add(btnHome);
        mainPanel.add(btnUpdateNew);

        add(mainPanel, BorderLayout.CENTER);
    }
}

class ViewStudentFrame extends JFrame {
    private final JFrame studentMgmtFrame;
    private final JFrame mainHomeFrame;
    private JTextField txtSearchId, txtName, txtNic, txtPrfMarks, txtDbmsMarks, txtGpa;
    private JLabel lblName, lblNic, lblPrf, lblDbms, lblGpa;
    private JButton btnBack;

    public ViewStudentFrame(JFrame studentMgmtFrame, JFrame mainHomeFrame) {
        this.studentMgmtFrame = studentMgmtFrame;
        this.mainHomeFrame = mainHomeFrame;

        setTitle("View Student");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("View Student");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setLayout(null);

        txtSearchId = new JTextField(" Enter Student Registration No");
        txtSearchId.setFont(new Font("SansSerif", Font.ITALIC, 14));
        txtSearchId.setForeground(Color.GRAY);
        txtSearchId.setBounds(130, 30, 350, 35);

        txtSearchId.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (txtSearchId.getText().equals(" Enter Student Registration No")) {
                    txtSearchId.setText("");
                    txtSearchId.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    txtSearchId.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (txtSearchId.getText().trim().isEmpty()) {
                    txtSearchId.setFont(new Font("SansSerif", Font.ITALIC, 14));
                    txtSearchId.setForeground(Color.GRAY);
                    txtSearchId.setText(" Enter Student Registration No");
                }
            }
        });
        contentPanel.add(txtSearchId);

        JButton btnSearch = new JButton("SEARCH");
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSearch.setBackground(new Color(24, 38, 115));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setBounds(500, 30, 180, 35);
        contentPanel.add(btnSearch);

        lblName = new JLabel("Name");
        lblName.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblName.setBounds(80, 90, 120, 30);
        lblName.setVisible(false);
        contentPanel.add(lblName);

        txtName = createDisplayField(200, 90, 420, 32, contentPanel);

        lblNic = new JLabel("NIC");
        lblNic.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNic.setBounds(80, 135, 120, 30);
        lblNic.setVisible(false);
        contentPanel.add(lblNic);

        txtNic = createDisplayField(200, 135, 230, 32, contentPanel);

        lblPrf = new JLabel("PRF Marks");
        lblPrf.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPrf.setBounds(80, 180, 120, 30);
        lblPrf.setVisible(false);
        contentPanel.add(lblPrf);

        txtPrfMarks = createDisplayField(200, 180, 230, 32, contentPanel);

        lblDbms = new JLabel("DBMS Marks");
        lblDbms.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblDbms.setBounds(80, 225, 120, 30);
        lblDbms.setVisible(false);
        contentPanel.add(lblDbms);

        txtDbmsMarks = createDisplayField(200, 225, 230, 32, contentPanel);

        lblGpa = new JLabel("GPA");
        lblGpa.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblGpa.setBounds(80, 270, 120, 30);
        lblGpa.setVisible(false);
        contentPanel.add(lblGpa);

        txtGpa = createDisplayField(200, 270, 230, 32, contentPanel);

        btnBack = new JButton("BACK TO HOME PAGE");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnBack.setBackground(new Color(24, 38, 115));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setBounds(60, 340, 260, 38);
        btnBack.setVisible(false);
        contentPanel.add(btnBack);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        btnBack.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        btnSearch.addActionListener(e -> {
            String searchId = txtSearchId.getText().trim();
            Student student = StudentRepository.findById(searchId);

            if (student == null) {
                JOptionPane.showMessageDialog(this, "This student not exist in the system", "ERROR", JOptionPane.ERROR_MESSAGE);
                setDetailsVisible(false);
            } else {
                txtName.setText(student.getName());
                txtNic.setText(student.getNic());
                
                txtPrfMarks.setText(student.getPrfMarks() == -1 ? "Absent" : (student.getPrfMarks() == -2 ? "Not Conducted" : String.valueOf(student.getPrfMarks())));
                txtDbmsMarks.setText(student.getDbmsMarks() == -1 ? "Absent" : (student.getDbmsMarks() == -2 ? "Not Conducted" : String.valueOf(student.getDbmsMarks())));
                
                txtGpa.setText(String.format("%.2f", student.calculateGpa()));
                setDetailsVisible(true);
            }
        });
    }

    private JTextField createDisplayField(int x, int y, int width, int height, JPanel panel) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.BOLD, 14));
        tf.setEditable(false);
        tf.setBackground(Color.WHITE);
        tf.setBounds(x, y, width, height);
        tf.setVisible(false);
        panel.add(tf);
        return tf;
    }

    private void setDetailsVisible(boolean visible) {
        lblName.setVisible(visible);
        txtName.setVisible(visible);
        lblNic.setVisible(visible);
        txtNic.setVisible(visible);
        lblPrf.setVisible(visible);
        txtPrfMarks.setVisible(visible);
        lblDbms.setVisible(visible);
        txtDbmsMarks.setVisible(visible);
        lblGpa.setVisible(visible);
        txtGpa.setVisible(visible);
        btnBack.setVisible(visible);
    }
}

class DeleteStudentFrame extends JFrame {
    private final JFrame studentMgmtFrame;
    private final JFrame mainHomeFrame;
    private JTextField txtSearchId, txtName, txtNic, txtPrfMarks, txtDbmsMarks, txtGpa;
    private JLabel lblName, lblNic, lblPrf, lblDbms, lblGpa;
    private JButton btnCancel, btnDelete;
    private Student currentStudent;

    public DeleteStudentFrame(JFrame studentMgmtFrame, JFrame mainHomeFrame) {
        this.studentMgmtFrame = studentMgmtFrame;
        this.mainHomeFrame = mainHomeFrame;

        setTitle("Delete Student");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Delete Student");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setLayout(null);

        txtSearchId = new JTextField(" Enter Student Registration No");
        txtSearchId.setFont(new Font("SansSerif", Font.ITALIC, 14));
        txtSearchId.setForeground(Color.GRAY);
        txtSearchId.setBounds(130, 30, 350, 35);

        txtSearchId.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (txtSearchId.getText().equals(" Enter Student Registration No")) {
                    txtSearchId.setText("");
                    txtSearchId.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    txtSearchId.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (txtSearchId.getText().trim().isEmpty()) {
                    txtSearchId.setFont(new Font("SansSerif", Font.ITALIC, 14));
                    txtSearchId.setForeground(Color.GRAY);
                    txtSearchId.setText(" Enter Student Registration No");
                }
            }
        });
        contentPanel.add(txtSearchId);

        JButton btnSearch = new JButton("SEARCH");
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSearch.setBackground(new Color(24, 38, 115));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setBounds(500, 30, 180, 35);
        contentPanel.add(btnSearch);

        lblName = new JLabel("Name");
        lblName.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblName.setBounds(80, 85, 120, 30);
        lblName.setVisible(false);
        contentPanel.add(lblName);

        txtName = createDisplayField(200, 85, 420, 30, contentPanel);

        lblNic = new JLabel("NIC");
        lblNic.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNic.setBounds(80, 125, 120, 30);
        lblNic.setVisible(false);
        contentPanel.add(lblNic);

        txtNic = createDisplayField(200, 125, 230, 30, contentPanel);

        lblPrf = new JLabel("PRF Marks");
        lblPrf.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPrf.setBounds(80, 165, 120, 30);
        lblPrf.setVisible(false);
        contentPanel.add(lblPrf);

        txtPrfMarks = createDisplayField(200, 165, 230, 30, contentPanel);

        lblDbms = new JLabel("DBMS Marks");
        lblDbms.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblDbms.setBounds(80, 205, 120, 30);
        lblDbms.setVisible(false);
        contentPanel.add(lblDbms);

        txtDbmsMarks = createDisplayField(200, 205, 230, 30, contentPanel);

        lblGpa = new JLabel("GPA");
        lblGpa.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblGpa.setBounds(80, 245, 120, 30);
        lblGpa.setVisible(false);
        contentPanel.add(lblGpa);

        txtGpa = createDisplayField(200, 245, 230, 30, contentPanel);

        btnCancel = new JButton("CANCEL");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancel.setBackground(new Color(24, 38, 115));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBounds(310, 330, 180, 38);
        btnCancel.setVisible(false);
        contentPanel.add(btnCancel);

        btnDelete = new JButton("DELETE");
        btnDelete.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnDelete.setBackground(new Color(24, 38, 115));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setBounds(520, 330, 210, 38);
        btnDelete.setVisible(false);
        contentPanel.add(btnDelete);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> {
            studentMgmtFrame.setVisible(true);
            dispose();
        });

        btnSearch.addActionListener(e -> {
            String searchId = txtSearchId.getText().trim();
            currentStudent = StudentRepository.findById(searchId);

            if (currentStudent == null) {
                JOptionPane.showMessageDialog(this, "This student does not exist in the system!", "ERROR", JOptionPane.ERROR_MESSAGE);
                setDetailsVisible(false);
            } else {
                txtName.setText(currentStudent.getName());
                txtNic.setText(currentStudent.getNic());
                txtPrfMarks.setText(currentStudent.getPrfMarks() == -1 ? "Absent" : (currentStudent.getPrfMarks() == -2 ? "Not Conducted" : String.valueOf(currentStudent.getPrfMarks())));
                txtDbmsMarks.setText(currentStudent.getDbmsMarks() == -1 ? "Absent" : (currentStudent.getDbmsMarks() == -2 ? "Not Conducted" : String.valueOf(currentStudent.getDbmsMarks())));
                txtGpa.setText(String.format("%.2f", currentStudent.calculateGpa()));
                setDetailsVisible(true);
            }
        });

        btnDelete.addActionListener(e -> {
            if (currentStudent == null) return;

            String deletedId = currentStudent.getStudentId();
            boolean success = StudentRepository.deleteStudent(deletedId);

            if (success) {
                new DeleteSuccessFrame(deletedId, studentMgmtFrame, mainHomeFrame).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student profile!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JTextField createDisplayField(int x, int y, int width, int height, JPanel panel) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.BOLD, 14));
        tf.setEditable(false);
        tf.setBackground(Color.WHITE);
        tf.setBounds(x, y, width, height);
        tf.setVisible(false);
        panel.add(tf);
        return tf;
    }

    private void setDetailsVisible(boolean visible) {
        lblName.setVisible(visible);
        txtName.setVisible(visible);
        lblNic.setVisible(visible);
        txtNic.setVisible(visible);
        lblPrf.setVisible(visible);
        txtPrfMarks.setVisible(visible);
        lblDbms.setVisible(visible);
        txtDbmsMarks.setVisible(visible);
        lblGpa.setVisible(visible);
        txtGpa.setVisible(visible);
        btnCancel.setVisible(visible);
        btnDelete.setVisible(visible);
    }
}

class DeleteSuccessFrame extends JFrame {
    public DeleteSuccessFrame(String regNo, JFrame studentMgmtFrame, JFrame mainHomeFrame) {
        setTitle("Delete Successfully");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(24, 38, 115));
        mainPanel.setLayout(null);

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBounds(25, 15, 100, 30);
        mainPanel.add(logoLabel);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(242, 246, 255));
        cardPanel.setBounds(80, 140, 625, 130);

        JLabel successMsg = new JLabel(regNo + " Successfully Deleted!");
        successMsg.setFont(new Font("SansSerif", Font.BOLD, 20));
        successMsg.setForeground(Color.BLACK);

        cardPanel.add(successMsg);
        mainPanel.add(cardPanel);

        JButton btnHome = new JButton("BACK TO HOME PAGE");
        btnHome.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnHome.setForeground(Color.WHITE);
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setBounds(25, 395, 220, 35);

        btnHome.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        JButton btnDeleteNew = new JButton("DELETE NEW STUDENT");
        btnDeleteNew.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnDeleteNew.setForeground(Color.WHITE);
        btnDeleteNew.setContentAreaFilled(false);
        btnDeleteNew.setBorderPainted(false);
        btnDeleteNew.setFocusPainted(false);
        btnDeleteNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeleteNew.setBounds(515, 395, 230, 35);

        btnDeleteNew.addActionListener(e -> {
            new DeleteStudentFrame(studentMgmtFrame, mainHomeFrame).setVisible(true);
            dispose();
        });

        mainPanel.add(btnHome);
        mainPanel.add(btnDeleteNew);

        add(mainPanel, BorderLayout.CENTER);
    }
}

class BatchManagementFrame extends JFrame {
    private final JFrame homeFrame;

    public BatchManagementFrame(JFrame homeFrame) {
        this.homeFrame = homeFrame;

        setTitle("Batch Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left Panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        JPanel textGroup = new JPanel();
        textGroup.setBackground(Color.WHITE);
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));

        JLabel titlePart1 = new JLabel("iCET");
        titlePart1.setFont(new Font("SansSerif", Font.BOLD, 42));
        titlePart1.setForeground(new Color(15, 23, 42));
        titlePart1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart2 = new JLabel("Learning");
        titlePart2.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart2.setForeground(new Color(30, 41, 59));
        titlePart2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart3 = new JLabel("Management System");
        titlePart3.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart3.setForeground(new Color(30, 41, 59));
        titlePart3.setAlignmentX(Component.CENTER_ALIGNMENT);

        textGroup.add(titlePart1);
        textGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        textGroup.add(titlePart2);
        textGroup.add(titlePart3);

        leftPanel.add(textGroup);

        // Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(24, 38, 115));

        JPanel menuGroup = new JPanel();
        menuGroup.setOpaque(false);
        menuGroup.setLayout(new BoxLayout(menuGroup, BoxLayout.Y_AXIS));

        JLabel mainHeading = new JLabel("Batch Management");
        mainHeading.setFont(new Font("SansSerif", Font.BOLD, 28));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeading = new JLabel("Choose an option from the menu below to get started");
        subHeading.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subHeading.setForeground(new Color(203, 213, 225));
        subHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuGroup.add(Box.createRigidArea(new Dimension(0, 40)));
        menuGroup.add(mainHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 8)));
        menuGroup.add(subHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnAddBatch = createMenuButton("Add Batches");
        JButton btnUpdateBatch = createMenuButton("Update Batches");
        JButton btnViewBatch = createMenuButton("View Batches Details");

        menuGroup.add(btnAddBatch);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        menuGroup.add(btnUpdateBatch);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        menuGroup.add(btnViewBatch);

        // Bottom Navigation
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        bottomPanel.setOpaque(false);

        JButton btnBack = new JButton("<- Back to Main Menu");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnBack.setForeground(Color.WHITE);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            homeFrame.setVisible(true);
            dispose();
        });

        bottomPanel.add(btnBack);

        rightPanel.add(menuGroup, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(leftPanel);
        add(rightPanel);

        btnAddBatch.addActionListener(e -> {
            new AddBatchFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });

        btnUpdateBatch.addActionListener(e -> {
            new UpdateBatchFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });

        btnViewBatch.addActionListener(e -> {
            new ViewBatchesFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(new Color(15, 23, 42));
        button.setBackground(new Color(219, 234, 254));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(280, 42));
        button.setPreferredSize(new Dimension(280, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}

class AddBatchFrame extends JFrame {
    private final JFrame batchMgmtFrame;
    private final JFrame mainHomeFrame;
    private JTextField txtBatchNum;

    public AddBatchFrame(JFrame batchMgmtFrame, JFrame mainHomeFrame) {
        this.batchMgmtFrame = batchMgmtFrame;
        this.mainHomeFrame = mainHomeFrame;

        setTitle("Add Batch");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Add Batch");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setLayout(null);

        JLabel lblBatch = new JLabel("Batch Number");
        lblBatch.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblBatch.setBounds(80, 80, 140, 30);
        contentPanel.add(lblBatch);

        txtBatchNum = new JTextField();
        txtBatchNum.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBatchNum.setBounds(230, 80, 250, 32);
        contentPanel.add(txtBatchNum);

        JButton btnCancel = new JButton("CANCEL");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancel.setBackground(new Color(24, 38, 115));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBounds(310, 330, 180, 38);
        contentPanel.add(btnCancel);

        JButton btnAdd = new JButton("ADD BATCH");
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAdd.setBackground(new Color(24, 38, 115));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setBounds(520, 330, 210, 38);
        contentPanel.add(btnAdd);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> {
            batchMgmtFrame.setVisible(true);
            dispose();
        });

        btnAdd.addActionListener(e -> {
            String bStr = txtBatchNum.getText().trim();
            if (bStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a batch number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int batchNo = Integer.parseInt(bStr);
                if (BatchRepository.exists(batchNo)) {
                    JOptionPane.showMessageDialog(this, "This Batch is already added to the system!", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    BatchRepository.addBatch(batchNo);
                    new AddBatchSuccessFrame(batchMgmtFrame, mainHomeFrame).setVisible(true);
                    dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Batch Number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

class AddBatchSuccessFrame extends JFrame {
    public AddBatchSuccessFrame(JFrame batchMgmtFrame, JFrame mainHomeFrame) {
        setTitle("Batch Added Successfully");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(24, 38, 115));
        mainPanel.setLayout(null);

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBounds(25, 15, 100, 30);
        mainPanel.add(logoLabel);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(242, 246, 255));
        cardPanel.setBounds(80, 140, 625, 130);

        JLabel successMsg = new JLabel("Batch is Successfully Added!");
        successMsg.setFont(new Font("SansSerif", Font.BOLD, 20));
        successMsg.setForeground(Color.BLACK);

        cardPanel.add(successMsg);
        mainPanel.add(cardPanel);

        JButton btnHome = new JButton("BACK TO HOME PAGE");
        btnHome.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnHome.setForeground(Color.WHITE);
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setBounds(25, 395, 220, 35);

        btnHome.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        JButton btnAddNew = new JButton("ADD NEW BATCH");
        btnAddNew.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAddNew.setForeground(Color.WHITE);
        btnAddNew.setContentAreaFilled(false);
        btnAddNew.setBorderPainted(false);
        btnAddNew.setFocusPainted(false);
        btnAddNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddNew.setBounds(535, 395, 210, 35);

        btnAddNew.addActionListener(e -> {
            new AddBatchFrame(batchMgmtFrame, mainHomeFrame).setVisible(true);
            dispose();
        });

        mainPanel.add(btnHome);
        mainPanel.add(btnAddNew);

        add(mainPanel, BorderLayout.CENTER);
    }
}

class UpdateBatchFrame extends JFrame {
    private final JFrame batchMgmtFrame;
    private final JFrame mainHomeFrame;
    private JTextField txtSearchBatch;
    private JTextField txtCount;
    private JRadioButton rbOpen, rbClosed;
    private ButtonGroup bgStatus;
    private JLabel lblCount, lblStatus;
    private JButton btnUpdate, btnCancel;
    private int currentBatchNo = -1;

    public UpdateBatchFrame(JFrame batchMgmtFrame, JFrame mainHomeFrame) {
        this.batchMgmtFrame = batchMgmtFrame;
        this.mainHomeFrame = mainHomeFrame;

        setTitle("Update Batch");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Update Batch");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setLayout(null);

        txtSearchBatch = new JTextField(" Enter Batch No");
        txtSearchBatch.setFont(new Font("SansSerif", Font.ITALIC, 14));
        txtSearchBatch.setForeground(Color.GRAY);
        txtSearchBatch.setBounds(130, 40, 350, 35);

        txtSearchBatch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (txtSearchBatch.getText().equals(" Enter Batch No")) {
                    txtSearchBatch.setText("");
                    txtSearchBatch.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    txtSearchBatch.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (txtSearchBatch.getText().trim().isEmpty()) {
                    txtSearchBatch.setFont(new Font("SansSerif", Font.ITALIC, 14));
                    txtSearchBatch.setForeground(Color.GRAY);
                    txtSearchBatch.setText(" Enter Batch No");
                }
            }
        });
        contentPanel.add(txtSearchBatch);

        JButton btnSearch = new JButton("SEARCH");
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSearch.setBackground(new Color(24, 38, 115));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setBounds(500, 40, 180, 35);
        contentPanel.add(btnSearch);

        lblCount = new JLabel("Student Count");
        lblCount.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblCount.setBounds(80, 120, 140, 30);
        lblCount.setVisible(false);
        contentPanel.add(lblCount);

        txtCount = new JTextField();
        txtCount.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtCount.setEditable(false);
        txtCount.setBackground(Color.WHITE);
        txtCount.setBounds(230, 120, 150, 32);
        txtCount.setVisible(false);
        contentPanel.add(txtCount);

        lblStatus = new JLabel("Current Status");
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblStatus.setBounds(80, 180, 140, 30);
        lblStatus.setVisible(false);
        contentPanel.add(lblStatus);

        rbOpen = new JRadioButton("ENROLLMENT OPEN");
        rbOpen.setFont(new Font("SansSerif", Font.BOLD, 13));
        rbOpen.setOpaque(false);
        rbOpen.setBounds(230, 180, 180, 30);
        rbOpen.setVisible(false);

        rbClosed = new JRadioButton("ENROLLMENT CLOSED");
        rbClosed.setFont(new Font("SansSerif", Font.BOLD, 13));
        rbClosed.setOpaque(false);
        rbClosed.setBounds(230, 215, 200, 30);
        rbClosed.setVisible(false);

        bgStatus = new ButtonGroup();
        bgStatus.add(rbOpen);
        bgStatus.add(rbClosed);

        contentPanel.add(rbOpen);
        contentPanel.add(rbClosed);

        btnCancel = new JButton("CANCEL");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancel.setBackground(new Color(24, 38, 115));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBounds(310, 330, 180, 38);
        btnCancel.setVisible(false);
        contentPanel.add(btnCancel);

        btnUpdate = new JButton("UPDATE");
        btnUpdate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnUpdate.setBackground(new Color(24, 38, 115));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setBounds(520, 330, 210, 38);
        btnUpdate.setVisible(false);
        contentPanel.add(btnUpdate);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> {
            batchMgmtFrame.setVisible(true);
            dispose();
        });

        btnSearch.addActionListener(e -> {
            String bStr = txtSearchBatch.getText().trim();
            try {
                int bNo = Integer.parseInt(bStr);
                if (BatchRepository.exists(bNo)) {
                    currentBatchNo = bNo;
                    txtCount.setText(String.valueOf(BatchRepository.getStudentCountForBatch(bNo)));
                    int status = BatchRepository.getStatus(bNo);
                    if (status == 1) {
                        rbOpen.setSelected(true);
                    } else {
                        rbClosed.setSelected(true);
                    }
                    lblCount.setVisible(true);
                    txtCount.setVisible(true);
                    lblStatus.setVisible(true);
                    rbOpen.setVisible(true);
                    rbClosed.setVisible(true);
                    btnCancel.setVisible(true);
                    btnUpdate.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "This batch does not exist in the system", "ERROR", JOptionPane.ERROR_MESSAGE);
                    setDetailsVisible(false);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Batch Number!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            if (currentBatchNo != -1) {
                int newStatus = rbOpen.isSelected() ? 1 : 0;
                BatchRepository.updateStatus(currentBatchNo, newStatus);
                new UpdateBatchSuccessFrame(currentBatchNo, batchMgmtFrame, mainHomeFrame).setVisible(true);
                dispose();
            }
        });
    }

    private void setDetailsVisible(boolean visible) {
        lblCount.setVisible(visible);
        txtCount.setVisible(visible);
        lblStatus.setVisible(visible);
        rbOpen.setVisible(visible);
        rbClosed.setVisible(visible);
        btnCancel.setVisible(visible);
        btnUpdate.setVisible(visible);
    }
}

class UpdateBatchSuccessFrame extends JFrame {
    public UpdateBatchSuccessFrame(int batchNo, JFrame batchMgmtFrame, JFrame mainHomeFrame) {
        setTitle("Batch Status Updated Successfully");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(24, 38, 115));
        mainPanel.setLayout(null);

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBounds(25, 15, 100, 30);
        mainPanel.add(logoLabel);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(242, 246, 255));
        cardPanel.setBounds(80, 140, 625, 130);

        JLabel successMsg = new JLabel(batchNo + " Batch Status Successfully Updated!");
        successMsg.setFont(new Font("SansSerif", Font.BOLD, 20));
        successMsg.setForeground(Color.BLACK);

        cardPanel.add(successMsg);
        mainPanel.add(cardPanel);

        JButton btnHome = new JButton("BACK TO HOME PAGE");
        btnHome.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnHome.setForeground(Color.WHITE);
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setBounds(25, 395, 220, 35);

        btnHome.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        JButton btnUpdateNew = new JButton("UPDATE NEW BATCH");
        btnUpdateNew.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnUpdateNew.setForeground(Color.WHITE);
        btnUpdateNew.setContentAreaFilled(false);
        btnUpdateNew.setBorderPainted(false);
        btnUpdateNew.setFocusPainted(false);
        btnUpdateNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdateNew.setBounds(515, 395, 230, 35);

        btnUpdateNew.addActionListener(e -> {
            new UpdateBatchFrame(batchMgmtFrame, mainHomeFrame).setVisible(true);
            dispose();
        });

        mainPanel.add(btnHome);
        mainPanel.add(btnUpdateNew);

        add(mainPanel, BorderLayout.CENTER);
    }
}

class ViewBatchesFrame extends JFrame {
    public ViewBatchesFrame(JFrame batchMgmtFrame, JFrame mainHomeFrame) {
        setTitle("View Batches");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("View Batches");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        String[] columns = {"No", "Batch No", "Student Count", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (int i = 0; i < BatchRepository.batchNumbers.size(); i++) {
            int bNo = BatchRepository.batchNumbers.get(i);
            int count = BatchRepository.getStudentCountForBatch(bNo);
            int status = BatchRepository.batchStatuses.get(i);
            String statusStr = status == 1 ? "ENROLLMENT OPEN" : "ENROLLMENT CLOSED";
            model.addRow(new Object[]{i + 1, bNo, count, statusStr});
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 226, 240));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnBack = new JButton("BACK TO HOME PAGE");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnBack.setBackground(new Color(24, 38, 115));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        bottomPanel.add(btnBack);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}

class GradeManagementFrame extends JFrame {
    private final JFrame homeFrame;

    public GradeManagementFrame(JFrame homeFrame) {
        this.homeFrame = homeFrame;

        setTitle("Grade Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left Panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        JPanel textGroup = new JPanel();
        textGroup.setBackground(Color.WHITE);
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));

        JLabel titlePart1 = new JLabel("iCET");
        titlePart1.setFont(new Font("SansSerif", Font.BOLD, 42));
        titlePart1.setForeground(new Color(15, 23, 42));
        titlePart1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart2 = new JLabel("Learning");
        titlePart2.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart2.setForeground(new Color(30, 41, 59));
        titlePart2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart3 = new JLabel("Management System");
        titlePart3.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart3.setForeground(new Color(30, 41, 59));
        titlePart3.setAlignmentX(Component.CENTER_ALIGNMENT);

        textGroup.add(titlePart1);
        textGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        textGroup.add(titlePart2);
        textGroup.add(titlePart3);

        leftPanel.add(textGroup);

        // Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(24, 38, 115));

        JPanel menuGroup = new JPanel();
        menuGroup.setOpaque(false);
        menuGroup.setLayout(new BoxLayout(menuGroup, BoxLayout.Y_AXIS));

        JLabel mainHeading = new JLabel("Grade Management");
        mainHeading.setFont(new Font("SansSerif", Font.BOLD, 28));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeading = new JLabel("Choose an option from the menu below to get started");
        subHeading.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subHeading.setForeground(new Color(203, 213, 225));
        subHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuGroup.add(Box.createRigidArea(new Dimension(0, 50)));
        menuGroup.add(mainHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 8)));
        menuGroup.add(subHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 35)));

        JButton btnPrf = createMenuButton("PRF Marks Update");
        JButton btnDbms = createMenuButton("DBMS Marks Update");

        menuGroup.add(btnPrf);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        menuGroup.add(btnDbms);

        // Bottom Navigation
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        bottomPanel.setOpaque(false);

        JButton btnBack = new JButton("<- Back to Main Menu");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnBack.setForeground(Color.WHITE);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            homeFrame.setVisible(true);
            dispose();
        });

        bottomPanel.add(btnBack);

        rightPanel.add(menuGroup, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(leftPanel);
        add(rightPanel);

        btnPrf.addActionListener(e -> {
            new MarkUpdateFrame(this, homeFrame, "PRF").setVisible(true);
            setVisible(false);
        });

        btnDbms.addActionListener(e -> {
            new MarkUpdateFrame(this, homeFrame, "DBMS").setVisible(true);
            setVisible(false);
        });
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(new Color(15, 23, 42));
        button.setBackground(new Color(219, 234, 254));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(280, 42));
        button.setPreferredSize(new Dimension(280, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}

class MarkUpdateFrame extends JFrame {
    private final JFrame gradeMgmtFrame;
    private final JFrame mainHomeFrame;
    private final String subject;
    private JTextField txtSearchId, txtName, txtMarks;
    private JLabel lblName, lblMarks;
    private JButton btnUpdate, btnCancel;
    private Student currentStudent;

    public MarkUpdateFrame(JFrame gradeMgmtFrame, JFrame mainHomeFrame, String subject) {
        this.gradeMgmtFrame = gradeMgmtFrame;
        this.mainHomeFrame = mainHomeFrame;
        this.subject = subject;

        setTitle(subject + " Marks Update");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel(subject + " Marks Update");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setLayout(null);

        txtSearchId = new JTextField(" Enter Student Registration No");
        txtSearchId.setFont(new Font("SansSerif", Font.ITALIC, 14));
        txtSearchId.setForeground(Color.GRAY);
        txtSearchId.setBounds(130, 40, 350, 35);

        txtSearchId.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (txtSearchId.getText().equals(" Enter Student Registration No")) {
                    txtSearchId.setText("");
                    txtSearchId.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    txtSearchId.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent evt) {
                if (txtSearchId.getText().trim().isEmpty()) {
                    txtSearchId.setFont(new Font("SansSerif", Font.ITALIC, 14));
                    txtSearchId.setForeground(Color.GRAY);
                    txtSearchId.setText(" Enter Student Registration No");
                }
            }
        });
        contentPanel.add(txtSearchId);

        JButton btnSearch = new JButton("SEARCH");
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSearch.setBackground(new Color(24, 38, 115));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setBounds(500, 40, 180, 35);
        contentPanel.add(btnSearch);

        lblName = new JLabel("Name");
        lblName.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblName.setBounds(80, 120, 120, 30);
        lblName.setVisible(false);
        contentPanel.add(lblName);

        txtName = new JTextField();
        txtName.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtName.setEditable(false);
        txtName.setBackground(Color.WHITE);
        txtName.setBounds(200, 120, 420, 32);
        txtName.setVisible(false);
        contentPanel.add(txtName);

        lblMarks = new JLabel(subject + " Marks");
        lblMarks.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblMarks.setBounds(80, 180, 120, 30);
        lblMarks.setVisible(false);
        contentPanel.add(lblMarks);

        txtMarks = new JTextField();
        txtMarks.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtMarks.setBounds(200, 180, 230, 32);
        txtMarks.setVisible(false);
        contentPanel.add(txtMarks);

        btnCancel = new JButton("CANCEL");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancel.setBackground(new Color(24, 38, 115));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setBounds(310, 330, 180, 38);
        btnCancel.setVisible(false);
        contentPanel.add(btnCancel);

        btnUpdate = new JButton("UPDATE");
        btnUpdate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnUpdate.setBackground(new Color(24, 38, 115));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setBounds(520, 330, 210, 38);
        btnUpdate.setVisible(false);
        contentPanel.add(btnUpdate);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        btnCancel.addActionListener(e -> {
            gradeMgmtFrame.setVisible(true);
            dispose();
        });

        btnSearch.addActionListener(e -> {
            String searchId = txtSearchId.getText().trim();
            currentStudent = StudentRepository.findById(searchId);

            if (currentStudent == null) {
                JOptionPane.showMessageDialog(this, "This student does not exist in the system", "ERROR", JOptionPane.ERROR_MESSAGE);
                lblName.setVisible(false);
                txtName.setVisible(false);
                lblMarks.setVisible(false);
                txtMarks.setVisible(false);
                btnCancel.setVisible(false);
                btnUpdate.setVisible(false);
            } else {
                txtName.setText(currentStudent.getName());
                int currentMark = subject.equalsIgnoreCase("PRF") ? currentStudent.getPrfMarks() : currentStudent.getDbmsMarks();
                txtMarks.setText(currentMark >= 0 ? String.valueOf(currentMark) : "");

                lblName.setVisible(true);
                txtName.setVisible(true);
                lblMarks.setVisible(true);
                txtMarks.setVisible(true);
                btnCancel.setVisible(true);
                btnUpdate.setVisible(true);
            }
        });

        btnUpdate.addActionListener(e -> {
            if (currentStudent == null) return;
            String markStr = txtMarks.getText().trim();

            try {
                int mark = Integer.parseInt(markStr);
                if (mark < 0 || mark > 100) {
                    JOptionPane.showMessageDialog(this, "Marks should be between 0 and 100!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (subject.equalsIgnoreCase("PRF")) {
                    currentStudent.setPrfMarks(mark);
                } else {
                    currentStudent.setDbmsMarks(mark);
                }

                JOptionPane.showMessageDialog(this, subject + " Marks updated successfully for " + currentStudent.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
                gradeMgmtFrame.setVisible(true);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer for marks!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
// Report management
class ReportManagementFrame extends JFrame {
    private final JFrame homeFrame;

    public ReportManagementFrame(JFrame homeFrame) {
        this.homeFrame = homeFrame;

        setTitle("Report Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left Panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        JPanel textGroup = new JPanel();
        textGroup.setBackground(Color.WHITE);
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));

        JLabel titlePart1 = new JLabel("iCET");
        titlePart1.setFont(new Font("SansSerif", Font.BOLD, 42));
        titlePart1.setForeground(new Color(15, 23, 42));
        titlePart1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart2 = new JLabel("Learning");
        titlePart2.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart2.setForeground(new Color(30, 41, 59));
        titlePart2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart3 = new JLabel("Management System");
        titlePart3.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart3.setForeground(new Color(30, 41, 59));
        titlePart3.setAlignmentX(Component.CENTER_ALIGNMENT);

        textGroup.add(titlePart1);
        textGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        textGroup.add(titlePart2);
        textGroup.add(titlePart3);

        leftPanel.add(textGroup);

        // Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(24, 38, 115));

        JPanel menuGroup = new JPanel();
        menuGroup.setOpaque(false);
        menuGroup.setLayout(new BoxLayout(menuGroup, BoxLayout.Y_AXIS));

        JLabel mainHeading = new JLabel("Report Management");
        mainHeading.setFont(new Font("SansSerif", Font.BOLD, 28));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeading = new JLabel("Choose an option from the menu below to get started");
        subHeading.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subHeading.setForeground(new Color(203, 213, 225));
        subHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuGroup.add(Box.createRigidArea(new Dimension(0, 40)));
        menuGroup.add(mainHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 8)));
        menuGroup.add(subHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnRegReport = createMenuButton("Student Registration Report");
        JButton btnBatchReport = createMenuButton("Batch-wise Student Report");
        JButton btnIndustryReport = createMenuButton("Industry Training Eligibility Report");

        menuGroup.add(btnRegReport);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        menuGroup.add(btnBatchReport);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        menuGroup.add(btnIndustryReport);

        // Bottom Navigation
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        bottomPanel.setOpaque(false);

        JButton btnBack = new JButton("<- Back to Main Menu");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnBack.setForeground(Color.WHITE);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            homeFrame.setVisible(true);
            dispose();
        });

        bottomPanel.add(btnBack);

        rightPanel.add(menuGroup, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(leftPanel);
        add(rightPanel);

        btnRegReport.addActionListener(e -> {
            new StudentRegistrationReportFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });

        btnBatchReport.addActionListener(e -> {
            new BatchWiseReportFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });

        btnIndustryReport.addActionListener(e -> {
            new IndustryTrainingReportFrame(this, homeFrame).setVisible(true);
            setVisible(false);
        });
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(new Color(15, 23, 42));
        button.setBackground(new Color(219, 234, 254));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(290, 42));
        button.setPreferredSize(new Dimension(290, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}

class StudentRegistrationReportFrame extends JFrame {
    public StudentRegistrationReportFrame(JFrame reportMgmtFrame, JFrame mainHomeFrame) {
        setTitle("Student Registration Report");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(850, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Student Registration Report");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"No", "Registration No", "Student Name", "NIC", "PRF Marks", "DBMS Marks", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<Student> students = new ArrayList<>(StudentRepository.getAllStudents());
        Collections.sort(students, Comparator.comparing(Student::getName));

        int count = 1;
        for (Student s : students) {
            String prfStr = String.valueOf(s.getPrfMarks());
            String dbmsStr = String.valueOf(s.getDbmsMarks());
            String gpaStr = String.format("%.2f", s.calculateGpa());

            model.addRow(new Object[]{count++, s.getStudentId(), s.getName(), s.getNic(), prfStr, dbmsStr, gpaStr});
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(220, 226, 240));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 2) { 
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnBack = new JButton("BACK TO HOME PAGE");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnBack.setBackground(new Color(24, 38, 115));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        bottomPanel.add(btnBack);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}

class BatchWiseReportFrame extends JFrame {
    private final JFrame reportMgmtFrame;
    private final JFrame mainHomeFrame;

    public BatchWiseReportFrame(JFrame reportMgmtFrame, JFrame mainHomeFrame) {
        this.reportMgmtFrame = reportMgmtFrame;
        this.mainHomeFrame = mainHomeFrame;

        setTitle("Batch-wise Student Report");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left Panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        JPanel textGroup = new JPanel();
        textGroup.setBackground(Color.WHITE);
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));

        JLabel titlePart1 = new JLabel("iCET");
        titlePart1.setFont(new Font("SansSerif", Font.BOLD, 42));
        titlePart1.setForeground(new Color(15, 23, 42));
        titlePart1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart2 = new JLabel("Learning");
        titlePart2.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart2.setForeground(new Color(30, 41, 59));
        titlePart2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titlePart3 = new JLabel("Management System");
        titlePart3.setFont(new Font("SansSerif", Font.BOLD, 26));
        titlePart3.setForeground(new Color(30, 41, 59));
        titlePart3.setAlignmentX(Component.CENTER_ALIGNMENT);

        textGroup.add(titlePart1);
        textGroup.add(Box.createRigidArea(new Dimension(0, 15)));
        textGroup.add(titlePart2);
        textGroup.add(titlePart3);

        leftPanel.add(textGroup);

        // Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(24, 38, 115));

        JPanel menuGroup = new JPanel();
        menuGroup.setOpaque(false);
        menuGroup.setLayout(new BoxLayout(menuGroup, BoxLayout.Y_AXIS));

        JLabel mainHeading = new JLabel("Batch-wise Student Report");
        mainHeading.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeading = new JLabel("Choose an option from the menu below to get started");
        subHeading.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subHeading.setForeground(new Color(203, 213, 225));
        subHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuGroup.add(Box.createRigidArea(new Dimension(0, 20)));
        menuGroup.add(mainHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 5)));
        menuGroup.add(subHeading);
        menuGroup.add(Box.createRigidArea(new Dimension(0, 20)));

        for (int bNo : BatchRepository.batchNumbers) {
            JButton btnBatch = createMenuButton(bNo + " Batch");
            btnBatch.addActionListener(e -> {
                new BatchStudentReportFrame(bNo, reportMgmtFrame, mainHomeFrame).setVisible(true);
                setVisible(false);
            });
            menuGroup.add(btnBatch);
            menuGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Bottom Navigation
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        bottomPanel.setOpaque(false);

        JButton btnBack = new JButton("<- Back to Home Page");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnBack.setForeground(Color.WHITE);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        bottomPanel.add(btnBack);

        rightPanel.add(menuGroup, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(leftPanel);
        add(rightPanel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(new Color(15, 23, 42));
        button.setBackground(new Color(219, 234, 254));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(280, 36));
        button.setPreferredSize(new Dimension(280, 36));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}

class BatchStudentReportFrame extends JFrame {
    public BatchStudentReportFrame(int batchNo, JFrame reportMgmtFrame, JFrame mainHomeFrame) {
        setTitle(batchNo + " Batch Student Report");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(850, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel(batchNo + " Batch Student Report");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"No", "Registration No", "Student Name", "NIC", "PRF Marks", "DBMS Marks", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String bStr = String.valueOf(batchNo);
        int count = 1;
        for (Student s : StudentRepository.getAllStudents()) {
            if (s.getBatchNumber().contains(bStr)) {
                String prfStr = s.getPrfMarks() == -1 ? "ABSENT" : (s.getPrfMarks() == -2 ? "Not Conducted" : String.valueOf(s.getPrfMarks()));
                String dbmsStr = s.getDbmsMarks() == -1 ? "ABSENT" : (s.getDbmsMarks() == -2 ? "Not Conducted" : String.valueOf(s.getDbmsMarks()));
                String gpaStr = String.format("%.2f", s.calculateGpa());

                model.addRow(new Object[]{count++, s.getStudentId(), s.getName(), s.getNic(), prfStr, dbmsStr, gpaStr});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(220, 226, 240));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 2) { 
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnBack = new JButton("BACK TO HOME PAGE");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnBack.setBackground(new Color(24, 38, 115));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        bottomPanel.add(btnBack);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}

class IndustryTrainingReportFrame extends JFrame {
    public IndustryTrainingReportFrame(JFrame reportMgmtFrame, JFrame mainHomeFrame) {
        setTitle("Industry Training Eligibility Report");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 38, 115));
        headerPanel.setPreferredSize(new Dimension(850, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("iCET");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);


        JLabel titleLabel = new JLabel("Industry Training Eligibility Report");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(242, 246, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"No", "Registration No", "Student Name", "NIC", "PRF Marks", "DBMS Marks", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        int count = 1;
        for (Student s : StudentRepository.getAllStudents()) {
            double gpa = s.calculateGpa();
            if (gpa > 3.25 && s.getPrfMarks() > 50 && s.getDbmsMarks() > 50) {
                String prfStr = String.valueOf(s.getPrfMarks());
                String dbmsStr = String.valueOf(s.getDbmsMarks());
                String gpaStr = String.format("%.2f", gpa);

                model.addRow(new Object[]{count++, s.getStudentId(), s.getName(), s.getNic(), prfStr, dbmsStr, gpaStr});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(220, 226, 240));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 2) { 
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnBack = new JButton("BACK TO HOME PAGE");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnBack.setBackground(new Color(24, 38, 115));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            mainHomeFrame.setVisible(true);
            dispose();
        });

        bottomPanel.add(btnBack);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}
