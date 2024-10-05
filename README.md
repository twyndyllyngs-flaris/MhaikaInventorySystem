<h2 align="center"> This is our final output for 3rd year software engineer course. </h1>\

<h3 align="center"> This is not compiled or built with .exe so you have to set up on your local machine </h1>

<h3 align="center"> How to setup on IntelliJ IDEA </h2>
<h3 > 1. Download XAMPP and start both Apache and MySQL </h3>
<h3 > 2. After installing XAMPP, go to http://localhost/phpmyadmin/ </h3>
<h3> 3. Create a new database named "se_system". </h3>
<h3> 4. Select the database, select import at top, upload se_system.sql file on the root of project folder.  </h3>
<h3> 5. On your IDE (Mine is IntelliJ but the steps would be similar), go to File >  Project Structure >  Modules > Dependencies </h3>
<h3> 6. Under Dependencies tab, click the cross or add button > go to root project folder and add charm-glisten-6.0.2.jar, itextpdf-5.5.13.jar, jfoenix-9.0.10.jar, and mysql-connector-java-8.0.28.jar.  </h3>
<h3> 7. Add charm-glisten-6.0.2.jar, itextpdf-5.5.13.jar, jfoenix-9.0.10.jar, and mysql-connector-java-8.0.28.jar. After that, click Apply. </h3>
<h3> 8. Go to /src/main/java/com/example/se and open all files with Controller at the end of its name.   </h3>
<h3> 9. Modify the line of code Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", ""); - replace username and password</h3>
<h3> 10. Rebuild the project or simply run it. </h3>

<h2 align="center"> Here are the screenshots of how the application looks </h2>
