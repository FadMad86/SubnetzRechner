import java.util.Scanner;

/**
 * Die Klasse SubnetCalculator stellt Funktionen für die Berechnung von Subnetzinformationen bereit.
 */
public class SubnetCalculator {
  private String ipAddress;         // Die IP-Adresse, die eingegeben wird.
  private String subnetMask;        // Die Subnetzmaske, die eingegeben wird.
  private String binaryIpAddress;   // Die binäre Darstellung der IP-Adresse.
  private String binarySubnetMask;  // Die binäre Darstellung der Subnetzmaske.

  /**
   * Der Konstruktor initialisiert die Instanzvariablen.
   */
  public SubnetCalculator() {
    this.ipAddress = "";
    this.subnetMask = "";
    this.binaryIpAddress = "";
    this.binarySubnetMask = "";
  }

  /**
   * Die Methode readIpAddressAndSubnetMask liest die IP-Adresse und die Subnetzmaske vom Benutzer ein.
   * Dabei wird die Eingabe auf Gültigkeit geprüft und entsprechende Fehlermeldungen ausgegeben.
   */
  public void readIpAddressAndSubnetMask() {
    Scanner scanner = new Scanner(System.in);

    boolean isValidIpAdress = false;
    boolean isValidSubnetMask = false;

    // Schleife, um eine gültige IP-Adresse einzugeben
    while (!isValidIpAdress) {
      System.out.print("Geben Sie die IP-Adresse ein: (Format: xxx.xxx.xxx.xxx)");
      this.ipAddress = scanner.nextLine();

      try {
        validateIpAddressFormat();
        if (!ipAddress.equals("0.0.0.0")) {
          isValidIpAdress = true;
        } else {
          System.out.println("Die IP-Adresse 0.0.0.0 ist ungültig. Bitte geben Sie eine andere IP-Adresse ein.");
        }
      } catch (IllegalArgumentException e) {
        System.out.println("Fehlerhafte Eingabe: " + e.getMessage());
        System.out.println(("Bitte geben Sie die IP-Adresse erneut ein."));
      }
    }
    // Schleife, um eine gültige Subnetzmaske einzugeben
    while (!isValidSubnetMask) {
      System.out.println("Geben sie die Subnetzmaske ein (Format: xxx.xxx.xxx.xxx):");
      this.subnetMask = scanner.nextLine();

      try {
        validateSubnetMaskFormat();
        isValidSubnetMask = true;
      } catch (IllegalArgumentException e) {
        System.out.println("Fehlerhafte Eingabe: " + e.getMessage());
        System.out.println("Bitte geben Sie die Subnetzmaske erneut ein.");
      }
    }
  }

  // Methode zur Überprüfung des Formats der IP-Adresse
  private void validateIpAddressFormat() {
    String[] octets = ipAddress.split("\\.");

    // Überprüfen, ob die IP-Adresse aus genau 4 Oktetten besteht
    if (octets.length != 4) {
      throw new IllegalArgumentException("Ungültige IP-Adresse-Format. Verwenden Sie vier Oktetten.)");
    }
    // Überprüfen, ob jede Oktette im Bereich von 0 bis 255 liegt
    for (int i = 0; i < 4; i++) {
      try {
        int octetValue = Integer.parseInt(octets[i]);
        if (octetValue < 0 || octetValue > 255) {
          throw new IllegalArgumentException("Jede Oktette muss im Bereich von 0 bis 255 liegen.");
        }
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Ungültige Oktett-Format. Verwenden Sie nur numerische Werte.");
      }
    }
  }

  // Methode zur Überprüfung des Formats der Subnetzmaske
  private void validateSubnetMaskFormat() {
    // Trennen der Subnetzmaske in Oktetten
    String[] octets = subnetMask.split("\\.");

    // Überprüfen, ob die Subnetzmaske aus genau 4 Oktetten besteht
    if (octets.length != 4) {
      throw new IllegalArgumentException("Ungültiges Subnetzmaske-Format. Verwenden Sie vier Oktetten.");
    }

    // Überprüfen, ob die Subnetzmaske nicht 0.0.0.0 ist
    if (subnetMask.equals("0.0.0.0")) {
      throw new IllegalArgumentException("Die Subnetzmaske darf nicht 0.0.0.0 sein.");
    }

    // Überprüfen, ob jede Oktette im Bereich von 0 bis 255 liegt und gültige Potenzen von 2 sind
    int[] validMaskValues = {0, 128, 192, 224, 240, 248, 252, 254, 255};
    for (int i = 0; i < 4; i++) {
      try {
        int octetValue = Integer.parseInt(octets[i]);
        if (octetValue < 0 || octetValue > 255 || !isValidMaskValue(octetValue, validMaskValues)) {
          throw new IllegalArgumentException("Ungültiges Oktett der Subnetzmaske. Verwende Sie gültige Potenzen von 2.");
        }
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Ungültiges Oktett-Format der Subnetzmaske. Verwenden Sie nur numerische Werte.");
      }
    }
  }

  // Methode zur Überprüfung, ob ein Wert in einer Liste gültiger Werte enthalten ist
  private boolean isValidMaskValue(int value, int[] validValues) {
    for (int validValue : validValues) {
      if (value == validValue) {
        return true; // Der Wert ist gültig
      }
    }
    return false; // Der Wert ist nicht in der Liste gültiger Werte enthalten
  }

  // Methode zum Konvertieren und Anzeigen der Binärrepräsentation der IP-Adresse und Subnetzmaske
  public void convertAndDisplayBinaryRepresentation() {
    binaryIpAddress = convertToBinary(ipAddress);
    binarySubnetMask = convertToBinary(subnetMask);

    // Zeige die Dezimal- und Binärrepräsentation der IP-Adresse und Subnetzmaske
    System.out.println("Dezimale Darstellung der IP-Adresse: " + ipAddress);
    System.out.println("Binäre Darstellung der IP-Adresse: " + binaryIpAddress);
    System.out.println("Dezimale Darstellung der Subnetzmaske: " + subnetMask);
    System.out.println("Binäre Darstellung der Subnetzmaske: " + binarySubnetMask);

    // Berechne und zeige die Dezimalrepräsentation der Netzwerk-ID
    calculateAndDisplayNetworkId();

    // Berechne und zeige die Dezimalrepräsentation des Broadcasts
    calculateAndDisplayBroadcast();

    // Berechne und zeige die Anzahl der Hosts
    calculateAndDisplayNumberOfHosts();
  }

  // Methode zur Berechnung und Anzeige der Dezimalrepräsentation der Netzwerk-ID
  private void calculateAndDisplayNetworkId() {
    String networkIdDecimal = calculateNetworkId();
    System.out.println("Netzwerk-ID: " + networkIdDecimal);
  }

  // Methode zur Berechnung und Anzeige der Dezimalrepräsentation des Broadcasts
  private void calculateAndDisplayBroadcast() {
    String broadcastDecimal = calculateBroadcast();
    System.out.println("Broadcasts: " + broadcastDecimal);
  }

  // Methode zur Berechnung der Netzwerk-ID in Dezimal
  private String calculateNetworkId() {
    if (!isIpAddressValid() || !isSubnetMaskValid()) {
      return "";
    }

    // Teile die IP-Adresse und Subnetzmaske in Oktetten auf
    String[] ipOctets = ipAddress.split("\\.");
    String[] subnetMaskOctets = subnetMask.split("\\.");
    StringBuilder networkIdDecimal = new StringBuilder();

    // Berechne die Dezimalrepräsentation der Netzwerk-ID
    for (int i = 0; i < 4; i++) {
      // Konvertiere die Oktetten in Dezimalzahlen
      int ipOctetValue = Integer.parseInt(ipOctets[i]);
      int subnetMaskOctetValue = Integer.parseInt(subnetMaskOctets[i]);

      // Führe eine bitweise AND-Operation durch
      int networkIdOctetValue = ipOctetValue & subnetMaskOctetValue;

      // Füge den Oktett-Wert zur Dezimalrepräsentation hinzu
      networkIdDecimal.append(networkIdOctetValue).append(".");
    }

    // Entferne das letzte "." am Ende
    networkIdDecimal.deleteCharAt(networkIdDecimal.length() - 1);

    return networkIdDecimal.toString();
  }

  // Methode zur Berechnung des Broadcasts in Dezimal
  private String calculateBroadcast() {
    if (!isIpAddressValid() || !isSubnetMaskValid()) {
      return "";
    }

    // Teile die IP-Adresse und Subnetzmaske in Oktetten auf
    String[] ipOctets = ipAddress.split("\\.");
    String[] subnetMaskOctets = subnetMask.split("\\.");
    StringBuilder broadcastDecimal = new StringBuilder();

    // Berechne die Dezimalrepräsentation des Broadcasts
    for (int i = 0; i < 4; i++) {
      // Konvertiere die Oktetten in Dezimalzahlen
      int ipOctetValue = Integer.parseInt(ipOctets[i]);
      int subnetMaskOctetValue = Integer.parseInt(subnetMaskOctets[i]);

      // Führe eine bitweise AND-Operation durch und negiere die Subnetzmaske
      int broadcastOctetValue = (ipOctetValue & subnetMaskOctetValue) | (~subnetMaskOctetValue & 255);

      // Füge den Oktett-Wert zur Dezimalrepräsentation hinzu
      broadcastDecimal.append(broadcastOctetValue).append(".");
    }

    // Entferne das letzte "." am Ende
    broadcastDecimal.deleteCharAt(broadcastDecimal.length() - 1);

    return broadcastDecimal.toString();
  }

  // Methode zur Berechnung und Anzeige der Anzahl der Hosts im Subnetz
  private void calculateAndDisplayNumberOfHosts() {
    if (!isSubnetMaskValid()) {
      System.out.println("Die Anzahl der Hosts kann nicht berechnet werden. Die Subnetzmaske ist ungültig.");
      return;
    }

    // Teile die Subnetzmaske in Oktetten auf
    String[] subnetMaskOctets = subnetMask.split("\\.");
    int numberOfZeros = 0;

    // Zähle die Anzahl der Bits, die auf 0 gesetzt sind
    for (int i = 0; i < 4; i++) {
      int subnetMaskOctetValue = Integer.parseInt(subnetMaskOctets[i]);
      numberOfZeros += Integer.bitCount(subnetMaskOctetValue ^ 255);
    }

    // Berechne die Anzahl der Hosts im Subnetz
    int numberOfHosts = (int) Math.pow(2, numberOfZeros) - 2; // Subtrahiere 2 für Netzwerk-ID und Broadcast

    // Gebe die Anzahl der Hosts aus
    System.out.println("Anzahl der Hosts im Subnetz: " + numberOfHosts);
  }

  // Methode zur Überprüfung der Gültigkeit der IP-Adresse
  private boolean isIpAddressValid() {
    try {
      validateIpAddressFormat(); // Aufruf der Validierungsmethode
      return true; // Wenn keine Ausnahme auftritt, ist die IP-Adresse gültig
    } catch (IllegalArgumentException e) {
      System.out.println("Fehlerhafte IP-Adresse: " + e.getMessage()); // Ausgabe der Fehlermeldung
      return false; // Rückgabe, dass die IP-Adresse ungültig ist
    }
  }

  // Methode zur Überprüfung der Gültigkeit der Subnetzmaske
  private boolean isSubnetMaskValid() {
    try {
      validateSubnetMaskFormat(); // Aufruf der Validierungsmethode
      return true; // Wenn keine Ausnahme auftritt, ist die Subnetzmaske gültig
    } catch (IllegalArgumentException e) {
      System.out.println("Fehlerhafte Subnetzmaske: " + e.getMessage()); // Ausgabe der Fehlermeldung
      return false; // Rückgabe, dass die Subnetzmaske ungültig ist
    }
  }



  // Methode zur Konvertierung einer Dezimalzahl in Binär
    private String convertToBinary(String address) {
    String[] octets = address.split("\\.");
    StringBuilder binaryRepresentation = new StringBuilder();

      // Iteration über die Oktetten
    for (String octet : octets) {
      int decimalValue = Integer.parseInt(octet);
      StringBuilder binaryValue = new StringBuilder();

      // Umrechnung von Dezimal zu Binär mit Modulo
      for (int i = 0; i < 8; i++) {
        binaryValue.insert(0, decimalValue % 2); // Einfügen des Rests bei der Division durch 2 am Anfang
        decimalValue /= 2; // Aktualisierung der Dezimalzahl durch Division durch 2
        }

      binaryRepresentation.append(binaryValue); // Hinzufügen des Binärwerts zur Binärdarstellung

    }

    return binaryRepresentation.toString().trim(); // Rückgabe der Binärdarstellung als Zeichenkette
    }
  }
