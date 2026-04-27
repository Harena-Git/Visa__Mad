@echo off
echo ========================================
echo Test de Déploiement - Visa Backoffice
echo ========================================

cd /d "C:\Users\Harena\.windsurf\worktrees\Visa__Mad\Visa__Mad-a56d59ed\backoffice"

echo.
echo [1/5] Nettoyage complet du projet...
call mvn clean
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Le nettoyage a échoué!
    pause
    exit /b 1
)

echo.
echo [2/5] Compilation avec tests...
call mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: La compilation a échoué!
    pause
    exit /b 1
)

echo.
echo [3/5] Vérification des fichiers JPA corrigés...
echo Vérification des repositories...

REM Vérifier VisaRepository
findstr /C:"findByDemandeur(Demandeur)" "src\main\java\com\visa\backoffice\repository\VisaRepository.java" >nul
if %ERRORLEVEL% NEQ 0 (
    echo ✓ VisaRepository correctement modifié
) else (
    echo ✗ VisaRepository non corrigé
)

REM Vérifier PassportRepository
findstr /C:"findByDemandeur(Demandeur)" "src\main\java\com\visa\backoffice\repository\PassportRepository.java" >nul
if %ERRORLEVEL% NEQ 0 (
    echo ✓ PassportRepository correctement modifié
) else (
    echo ✗ PassportRepository non corrigé
)

REM Vérifier VisaTransformableRepository
findstr /C:"findByDemandeur(Demandeur)" "src\main\java\com\visa\backoffice\repository\VisaTransformableRepository.java" >nul
if %ERRORLEVEL% NEQ 0 (
    echo ✓ VisaTransformableRepository correctement modifié
) else (
    echo ✗ VisaTransformableRepository non corrigé
)

echo.
echo [4/5] Création du package WAR...
call mvn package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: La création du package a échoué!
    pause
    exit /b 1
)

echo.
echo [5/5] Déploiement vers TomCat...
if exist "target\backoffice-0.0.1-SNAPSHOT.war" (
    echo Copie du WAR vers TomCat...
    copy "target\backoffice-0.0.1-SNAPSHOT.war" "D:\Tom_Cat\webapps\visa-backoffice.war"
    if %ERRORLEVEL% EQU 0 (
        echo ✓ WAR copié avec succès
    ) else (
        echo ✗ Erreur lors de la copie du WAR
    )
) else (
    echo ✗ Fichier WAR non trouvé!
    pause
    exit /b 1
)

echo.
echo ========================================
echo RÉSUMÉ DES CORRECTIONS APPLIQUÉES
echo ========================================
echo.
echo Fichiers modifiés:
echo  - VisaRepository.java (findByDemandeur corrigé)
echo  - PassportRepository.java (findByDemandeur corrigé)
echo  - VisaTransformableRepository.java (findByDemandeur corrigé)
echo  - DemandeRepository.java (findByDemandeur corrigé)
echo  - StatutDemandeRepository.java (findByDemandeur corrigé)
echo  - CheckPieceRepository.java (findByDemandeur corrigé)
echo  - CarteResidenceRepository.java (findByDemandeur corrigé)
echo.
echo Services créés/modifiés:
echo  - VisaService.java (NOUVEAU)
echo  - VisaTransformableService.java (NOUVEAU)
echo  - StatutDemandeService.java (NOUVEAU)
echo  - DemandeService.java (findByIdDemandeur adapté)
echo  - PassportService.java (findByIdDemandeur adapté)
echo.
echo Configuration web:
echo  - WebConfig.java (NOUVEAU)
echo  - WebController.java (amélioré)
echo  - application-tomcat.properties (NOUVEAU)
echo.
echo ========================================
echo DÉPLOIEMENT TERMINÉ
echo ========================================
echo.
echo URLs de test:
echo http://localhost:8080/visa-backoffice/
echo http://localhost:8080/visa-backoffice/demandeurs
echo http://localhost:8080/visa-backoffice/demandes
echo.
echo Redémarrez TomCat si nécessaire...
pause
