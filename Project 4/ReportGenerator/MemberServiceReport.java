package ReportGenerator;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MemberServiceReport {
    private String MemberName, FileName, email;
    private int MemberNumber, WeekNumber;

    public MemberServiceReport(String MemberName, int MemberNumber, int WeekNumber, String email) {
        this.MemberName = MemberName;
        this.MemberNumber = MemberNumber;
        this.WeekNumber = WeekNumber;
        this.FileName = sanitizeFileName("M." + WeekNumber + "." + MemberName + "." + MemberNumber + ".txt");
        this.email = email;
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    public void read() {
        Path filepath = Paths.get("ReportCenter", FileName);
        if (!Files.exists(filepath)) {
            System.out.println("Error: File not found.");
            return;
        }
        try (Scanner reader = new Scanner(filepath.toFile())) {
            StringBuilder content = new StringBuilder();
            while (reader.hasNextLine()) content.append(reader.nextLine()).append("\n");
            System.out.printf("Simulating email to: %s\nSubject: Requested Report\nBody:\n%s\n", email, content);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

	public String readGUI() {
        Path filepath = Paths.get("ReportCenter", FileName);
        if (!Files.exists(filepath)) {
            System.out.println("Error: File not found.");
            return "";
        }
        try (Scanner reader = new Scanner(filepath.toFile())) {
            StringBuilder content = new StringBuilder();
			content.append("Simulating email\nTo: ").append(email).append("\nSubject: Requested Report\nBody: \n");
            while (reader.hasNextLine()) content.append(reader.nextLine()).append("\n");
			return content.toString();
            // System.out.printf("Simulating email to: %s\nSubject: Requested Report\nBody:\n%s\n", email, content);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
		return "";
    }

    public void create() {
        Path servicesFile = Paths.get("Billing", "AllServices.txt"), reportFile = Paths.get("ReportCenter", FileName);
        try {
            Files.createDirectories(reportFile.getParent());
            Map<String, String[]> memberData = loadMemberData("DataCenter/MemberData.txt");
            Set<String> writtenMembers = new HashSet<>();
            List<String> serviceEntries = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(servicesFile.toFile()));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(reportFile.toFile()))) {

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 11) continue;
                    String memberNumber = parts[6].trim();
                    if (!memberNumber.equals(String.valueOf(MemberNumber))) continue;  // Filter by MemberNumber

                    // Add member info once for this member
                    if (!writtenMembers.contains(memberNumber) && memberData.containsKey(memberNumber)) {
                        bw.write(String.join(",", memberData.get(memberNumber)));
                        bw.newLine();
                        writtenMembers.add(memberNumber);
                    }

                    // Add service entry for the specific member
                    String serviceEntry = String.join(",", parts[4].trim(), parts[0].trim(), parts[8].trim());
                    serviceEntries.add(serviceEntry);
                }

                // Write all service entries for this member
                for (String entry : serviceEntries) {
                    bw.write(entry);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }

    private Map<String, String[]> loadMemberData(String filePath) throws IOException {
        Map<String, String[]> memberDataMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) memberDataMap.put(parts[1].trim(), parts);
            }
        }
        return memberDataMap;
    }

    public static class ServiceData {
        private String dateService, providerName, serviceName;

        public ServiceData(String dateService, String providerName, String serviceName) {
            this.dateService = dateService;
            this.providerName = providerName;
            this.serviceName = serviceName;
        }

        @Override
        public String toString() {
            return String.join(",", dateService, providerName, serviceName);
        }
    }
}
