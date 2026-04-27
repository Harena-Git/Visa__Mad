@echo off
echo ========================================
echo Build et Deploiement Visa Backoffice
echo ========================================

cd /d "C:\Users\Harena\.windsurf\worktrees\Visa__Mad\Visa__Mad-a56d59ed\backoffice"

echo.
echo [1/4] Nettoyage du projet...
call mvn clean

echo.
echo [2/4] Compilation du projet...
call mvn compile

echo.
echo [3/4] Creation du WAR...
call mvn package -DskipTests

echo.
echo [4/4] Copie vers Tomcat...
if exist "target\backoffice-0.0.1-SNAPSHOT.war" (
    copy "target\backoffice-0.0.1-SNAPSHOT.war" "D:\Tom_Cat\webapps\visa-backoffice.war"
    echo WAR copie avec succes vers Tomcat!
) else (
    echo ERREUR: Fichier WAR non trouve!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Build termine avec succes!
echo ========================================
echo.
echo URLs de test:
echo http://localhost:8080/visa-backoffice/
echo http://localhost:8080/visa-backoffice/demandeurs
echo http://localhost:8080/visa-backoffice/demandes
echo.
echo Redemarrez Tomcat si necessaire...
pause
