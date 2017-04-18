package database;

//erstat konstanterne nedenfor

public abstract class Constant
{
	public static final String
	server					= "jdbc:mysql://ec2-52-30-89-247.eu-west-1.compute.amazonaws.com:3306/grp15",  // database-serveren
	database				= "cleanoutloud",  //"jdbcdatabase", // navnet paa din database = dit studienummer
	username				= "grp17", // dit brugernavn = dit studienummer 
	password				= "grp17"; // dit password som du har valgt til din database

	public static final int
	port					= 3306;
}
