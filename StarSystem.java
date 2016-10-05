/**
 * Basic star system class for the Prog4 driver. 
 */
import java.util.Vector;

public class StarSystem {

  public enum ColIndex {
    ROW_ID(0), // Externally used row index
    STAR_NAME(1), // Host system star name
    PLANET_LETTER(2), //
    DISCOVERY_METHOD(3), //
    NUMBER_PLANETS(4), //
    ORBITAL_PERIOD(5), //
    ORBIT_SEMI_MAJOR_AXIS(6), //
    ECCENTRICITY(7), //
    INCLINATION(8), //
    PLANET_MASS_J(9), //
    PLANET_MASS_PROV(10), //
    PLANET_RADIUS(11), //
    PLANET_DENSITY(12), //
    TTV_FLAG(13), //
    KEPLER_FLAG(14), //
    NUMBER_OF_NOTES(15), //
    RA_S(16), //
    RA_DEG(17), //
    DEC_S(18), //
    DEC_DEG(19), //
    DISTANCE(20), //
    OPTICAL_MAG(21), //
    OPTICAL_MAG_BAND(22), //
    EFFECTIVE_TEMP_K(23), //
    STELLAR_MASS(24), //
    STELLAR_RADIUS(25), //
    LAST_UPDATE_DATE(26);

    private final int index;

    private ColIndex(int index) {
      this.index = index;
    }

    public int index() {
      return index;
    }
  }

  public static class Planet {

    private String planetLetter;
    private double orbitalPeriod;

    private double planetMass;
    private String massProv;

    /**
     * Creates a planet with the indicated parameters.
     * 
     * @param letter
     *          letter in system (e.g. 'b')
     * @param mass
     *          Jupiter units
     * @param massP
     *          "Msini" or "Mass"
     * @param period
     *          Orbital period
     */
    public Planet(String letter, double mass, String massP, double period) {
      planetLetter = letter;
      planetMass = mass;
      if (massP.toUpperCase().equals("MSINI")) {
        massProv = "M * sin(i)";
      } else {
        massProv = "M";
      }
      orbitalPeriod = period;
    }

    /**
     * Copy constructor.
     * 
     * @param ref
     *          the planet to use as a template
     */
    public Planet(Planet ref) {
      planetLetter = ref.planetLetter;
      planetMass = ref.planetMass;
      massProv = ref.massProv;
      orbitalPeriod = ref.orbitalPeriod;
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append(String.format("[Planet %s]\n", planetLetter));
      sb.append(String.format("  Orbital Period: %.01f days\n", orbitalPeriod));
      sb.append(
          String.format("  Jupiter Mass: %.02f %s\n", planetMass, massProv));
      return sb.toString();
    }
  }

  protected double distanceInParsecs;
  protected String rightAscension;
  protected String declination;

  protected int numPlanetsInSystem;
  protected Vector<Planet> exoplanets;

  /**
   * Factory method creates and returns a new star system based upon the
   * provided reference and the csv update. Thus, as new planets appear in the
   * exoplanet file, they will build upon the previous entry for the system.
   * 
   * @param reference
   *          template system to use or null for an initial entry
   * @param csv
   *          the line from the csv exoplanet file to apply to the reference
   * @return a new StarSystem with the provided changes incorporated (i.e.
   *         planet added).
   */
  public static StarSystem updateFromArray(StarSystem reference, String[] csv) {

    StarSystem toAdd;

    if (reference != null) {
      toAdd = new StarSystem(reference);
    } else {
      toAdd = new StarSystem(csv[ColIndex.RA_S.index()],
          csv[ColIndex.DEC_S.index()]);
    }

    toAdd.setDistance(csv[ColIndex.DISTANCE.index()]);

    try {
      toAdd.updatePlanetCount(
          Integer.parseInt(csv[StarSystem.ColIndex.NUMBER_PLANETS.index()]));
    } catch (Exception e) {
    }

    if (toAdd.exoplanets == null) {
      toAdd.exoplanets = new Vector<Planet>();
    }

    double days = 0;

    double mass = 0;

    try {
      days = Double
          .parseDouble(csv[StarSystem.ColIndex.ORBITAL_PERIOD.index()]);
    } catch (Exception e) {
    }

    try {
      mass = Double.parseDouble(csv[StarSystem.ColIndex.PLANET_MASS_J.index()]);
    } catch (Exception e) {
    }

    toAdd.exoplanets
        .add(new Planet(csv[StarSystem.ColIndex.PLANET_LETTER.index()], mass,
            csv[StarSystem.ColIndex.PLANET_MASS_PROV.index()], days));

    return toAdd;
  }

  private StarSystem(StarSystem reference) {
    this(reference.rightAscension, reference.declination);
    numPlanetsInSystem = reference.numPlanetsInSystem;
    distanceInParsecs = reference.distanceInParsecs;

    exoplanets = new Vector<Planet>();
    for (Planet p : reference.exoplanets) {
      exoplanets.addElement(new Planet(p));
    }

  }

  private StarSystem(String ra, String dec) {
    rightAscension = ra;
    declination = dec;
  }

  private String positionData() {
    StringBuffer sb;
    sb = new StringBuffer();
    sb.append(String.format("Right Ascension: %s\nDeclination: %s\nDistance: ",
        rightAscension, declination));

    if (distanceInParsecs >= 0) {
      sb.append(String.format("%.01f pc", distanceInParsecs));
    } else {
      sb.append("far, far away . . . ");
    }

    return sb.toString();
  }

  /**
   * From Wikipedia: A parsec (symbol: pc) is a unit of length used to measure
   * the astronomically large distances to objects outside the Solar System. One
   * parsec is the distance at which one astronomical unit subtends an angle of
   * one arcsecond. A parsec is equal to about 3.26 light-years (31 trillion
   * kilometres or 19 trillion miles) in length. The nearest star, Proxima
   * Centauri, is about 1.3 parsecs from the Sun. Most of the stars visible to
   * the unaided eye in the nighttime sky are within 500 parsecs of the Sun.
   * 
   * @see <a href="https://en.wikipedia.org/wiki/Parsec">Wikipedia</a>
   * @see The Kessel Run
   * @param dist
   *          A string representation of a double to use as the distance in pc
   * @return false if unable to
   */
  public boolean setDistance(String dist) {
    try {
      distanceInParsecs = Double.parseDouble(dist);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public void updatePlanetCount(int count) {
    if (count > numPlanetsInSystem) {
      numPlanetsInSystem = count;
    }
  }

  @Override
  public String toString() {
    StringBuffer sb;
    sb = new StringBuffer();
    sb.append(positionData());
    sb.append("\n");
    sb.append(String.format("\nNumber of planets: %d", numPlanetsInSystem));
    if (exoplanets != null) {
      for (Planet p : exoplanets) {
        sb.append("\n" + p);
      }
    }
    return sb.toString();
  }
}
