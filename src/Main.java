
public class Main {
    public static void main(String[] args) {

        // Erstellen einer Instanz des Subnetzrechners
        SubnetCalculator subnetCalculator = new SubnetCalculator();

        // Eingabeaufforderung und Einlesen der IP-Adresse und Subnetzmaske
        subnetCalculator.readIpAddressAndSubnetMask();

        // Konvertierung und Anzeige der Binärdarstellung von IP-Adresse und Subnetzmaske
        subnetCalculator.convertAndDisplayBinaryRepresentation();

    }

}
