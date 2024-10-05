<h3 align="center"> This is our final output for 3rd year software engineer course. </h3>

<h3 align="center"> This is not compiled or built with .exe so you have to set up on your local machine </h3>

<h3 align="center"> How to setup on IntelliJ IDEA </h3>
<h5> 1. Download XAMPP and start both Apache and MySQL </h5>
<h5> 2. After installing XAMPP, go to http://localhost/phpmyadmin/ </h5>
<h5> 3. Create a new database named "se_system". </h5>
<h5> 4. Select the database, select import at top, upload se_system.sql file on the root of project folder.  </h5>
<h5> 5. On your IDE (Mine is IntelliJ but the steps would be similar), go to File >  Project Structure >  Modules > Dependencies </h5>
<h5> 6. Under Dependencies tab, click the cross or add button > go to root project folder and add charm-glisten-6.0.2.jar, itextpdf-5.5.13.jar, jfoenix-9.0.10.jar, and mysql-connector-java-8.0.28.jar.  </h5>
<h5> 7. Add charm-glisten-6.0.2.jar, itextpdf-5.5.13.jar, jfoenix-9.0.10.jar, and mysql-connector-java-8.0.28.jar. After that, click Apply. </h5>
<h5> 8. Go to /src/main/java/com/example/se and open all files with Controller at the end of its name.   </h5>
<h5> 9. Modify the line of code Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/se_system", "root", ""); - replace username and password</h5>
<h5> 10. Rebuild the project or simply run it. </h5>

<h3 align="center"> Here are the screenshots of how the application looks </h3>
