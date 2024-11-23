-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema u400425564_ccinfo208db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema u400425564_ccinfo208db
-- -----------------------------------------------------
USE `u400425564_ccinfo208db` ;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`barangay`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`barangay` (
  `barangayID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `barangayName` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`barangayID`),
  UNIQUE INDEX `barangayName_UNIQUE` (`barangayName` ASC) VISIBLE,
  UNIQUE INDEX `barangayID_UNIQUE` (`barangayID` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`bhw`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`bhw` (
  `bhwID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `lastname` VARCHAR(45) NOT NULL,
  `firstname` VARCHAR(45) NOT NULL,
  `middlename` VARCHAR(45) NOT NULL,
  `barangay_assignedto` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`bhwID`),
  UNIQUE INDEX `bhwID_UNIQUE` (`bhwID` ASC) VISIBLE,
  INDEX `Barangay FK_idx` (`barangay_assignedto` ASC) VISIBLE,
  CONSTRAINT `barangay_FK_bhw`
    FOREIGN KEY (`barangay_assignedto`)
    REFERENCES `u400425564_ccinfo208db`.`barangay` (`barangayID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`closecontact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`closecontact` (
  `closecontactID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `lastname` VARCHAR(45) NOT NULL,
  `firstname` VARCHAR(45) NOT NULL,
  `middlename` VARCHAR(45) NOT NULL,
  `birthday` DATE NOT NULL,
  `sex` ENUM('M', 'F') NOT NULL,
  `involved_bhw` INT UNSIGNED NOT NULL,
  `is_elevated_to_patient` ENUM('Y', 'N') NOT NULL DEFAULT 'N',
  PRIMARY KEY (`closecontactID`),
  CONSTRAINT `bhwID_FK_cc`
    FOREIGN KEY (`involved_bhw`)
    REFERENCES `u400425564_ccinfo208db`.`bhw` (`bhwID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`currentaddress`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`currentaddress` (
  `addressID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ca_houseno` VARCHAR(45) NOT NULL,
  `ca_subdivision` VARCHAR(45) NULL,
  `ca_streetname` VARCHAR(45) NOT NULL,
  `ca_barangay` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`addressID`),
  UNIQUE INDEX `addressID_UNIQUE` (`addressID` ASC) VISIBLE,
  CONSTRAINT `barangay_FK_ca`
    FOREIGN KEY (`ca_barangay`)
    REFERENCES `u400425564_ccinfo208db`.`barangay` (`barangayID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`patient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`patient` (
  `patientID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `lastname` VARCHAR(45) NOT NULL,
  `firstname` VARCHAR(45) NOT NULL,
  `middlename` VARCHAR(45) NOT NULL,
  `birthday` DATE NOT NULL,
  `sex` ENUM('M', 'F') NOT NULL,
  `is_bcg_vaccinated` ENUM('Y', 'N') NOT NULL,
  `addressID` INT UNSIGNED NOT NULL,
  `closecontactcaseID` INT UNSIGNED NULL,
  PRIMARY KEY (`patientID`),
  UNIQUE INDEX `patientID_UNIQUE` (`patientID` ASC) VISIBLE,
  CONSTRAINT `addressID_FK_patient`
    FOREIGN KEY (`addressID`)
    REFERENCES `u400425564_ccinfo208db`.`currentaddress` (`addressID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ccID_FK_patient`
    FOREIGN KEY (`closecontactcaseID`)
    REFERENCES `u400425564_ccinfo208db`.`closecontact` (`closecontactID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`physician`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`physician` (
  `physician_prc_id` DECIMAL(7,0) UNSIGNED NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `firstname` VARCHAR(45) NOT NULL,
  `middlename` VARCHAR(45) NOT NULL,
  `barangay_assignedto` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`physician_prc_id`),
  INDEX `Barangay FK_idx` (`barangay_assignedto` ASC) VISIBLE,
  UNIQUE INDEX `physician_prc_id_UNIQUE` (`physician_prc_id` ASC) VISIBLE,
  CONSTRAINT `barangay_FK_physician`
    FOREIGN KEY (`barangay_assignedto`)
    REFERENCES `u400425564_ccinfo208db`.`barangay` (`barangayID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`medicalcenter`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`medicalcenter` (
  `medicalcenterID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `located_at_barangay` INT UNSIGNED NOT NULL,
  `landline_no` VARCHAR(15) NOT NULL,
  `mobile_no` VARCHAR(15) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`medicalcenterID`),
  UNIQUE INDEX `medicalcenterID_UNIQUE` (`medicalcenterID` ASC) VISIBLE,
  CONSTRAINT `barangay_FK_medicalcenter`
    FOREIGN KEY (`located_at_barangay`)
    REFERENCES `u400425564_ccinfo208db`.`barangay` (`barangayID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`patient_case`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`patient_case` (
  `patientcaseno` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `patientID` INT UNSIGNED NOT NULL,
  `case_status` ENUM('Ongoing', 'Cured', 'On Treatment', 'Closed - Died', 'Closed - Lost to Followup', 'Closed - Transferred', 'Closed - Archived') NOT NULL DEFAULT 'Ongoing',
  `start_date` DATE NOT NULL,
  `involved_physician` DECIMAL(7,0) UNSIGNED NOT NULL,
  `involved_bhw` INT UNSIGNED NOT NULL,
  `end_date` DATE NULL,
  `toBePrescribed` ENUM('T', 'F') NOT NULL DEFAULT 'T',
  `diagnosis_date` DATE NULL,
  `finalDiagnosis` ENUM('DS - TB', 'DR - TB') NULL,
  `transferred_to_medical_center` INT UNSIGNED NULL,
  PRIMARY KEY (`patientcaseno`, `patientID`),
  INDEX `physician FK_idx` (`involved_physician` ASC) VISIBLE,
  UNIQUE INDEX `patientcaseno_UNIQUE` (`patientcaseno` ASC) VISIBLE,
  CONSTRAINT `patientID_FK_patientcase`
    FOREIGN KEY (`patientID`)
    REFERENCES `u400425564_ccinfo208db`.`patient` (`patientID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `physician_FK_patientcase`
    FOREIGN KEY (`involved_physician`)
    REFERENCES `u400425564_ccinfo208db`.`physician` (`physician_prc_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `bhw_FK_patientcase`
    FOREIGN KEY (`involved_bhw`)
    REFERENCES `u400425564_ccinfo208db`.`bhw` (`bhwID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `medicalcenter_FK_pc`
    FOREIGN KEY (`transferred_to_medical_center`)
    REFERENCES `u400425564_ccinfo208db`.`medicalcenter` (`medicalcenterID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`pc_closecontact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`pc_closecontact` (
  `closecontactID` INT UNSIGNED NOT NULL,
  `patientcaseno` INT UNSIGNED NOT NULL,
  `relationshipToPatient` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`closecontactID`, `patientcaseno`),
  INDEX `patientID FK_idx` (`patientcaseno` ASC) VISIBLE,
  CONSTRAINT `closecontactID_FK_cc`
    FOREIGN KEY (`closecontactID`)
    REFERENCES `u400425564_ccinfo208db`.`closecontact` (`closecontactID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `patientcaseno_FK_patientcc`
    FOREIGN KEY (`patientcaseno`)
    REFERENCES `u400425564_ccinfo208db`.`patient_case` (`patientcaseno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`ref_comorbidity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`ref_comorbidity` (
  `comorbidityID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `comorbidity` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`comorbidityID`),
  UNIQUE INDEX `comorbidity_UNIQUE` (`comorbidity` ASC) VISIBLE,
  UNIQUE INDEX `comorbidityID_UNIQUE` (`comorbidityID` ASC) VISIBLE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`patient_comorbidity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`patient_comorbidity` (
  `patientID` INT UNSIGNED NOT NULL,
  `comorbidityID` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`patientID`, `comorbidityID`),
  CONSTRAINT `patientID_FK_pcomorbidity`
    FOREIGN KEY (`patientID`)
    REFERENCES `u400425564_ccinfo208db`.`patient` (`patientID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `comorbidityID_FK_patientcomorbidity`
    FOREIGN KEY (`comorbidityID`)
    REFERENCES `u400425564_ccinfo208db`.`ref_comorbidity` (`comorbidityID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`prescription`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`prescription` (
  `prescription_serialno` VARCHAR(12) NOT NULL,
  `caseno` INT UNSIGNED NOT NULL,
  `date_given` DATE NOT NULL,
  PRIMARY KEY (`prescription_serialno`),
  UNIQUE INDEX `prescription_serialno_UNIQUE` (`prescription_serialno` ASC) VISIBLE,
  CONSTRAINT `caseno_FK_prescription`
    FOREIGN KEY (`caseno`)
    REFERENCES `u400425564_ccinfo208db`.`patient_case` (`patientcaseno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`ref_medicine`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`ref_medicine` (
  `generic_name` VARCHAR(100) NOT NULL,
  `medicine_desc` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`generic_name`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`prescription_medicine`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`prescription_medicine` (
  `prescription_serialno` VARCHAR(12) NOT NULL,
  `generic_name` VARCHAR(100) NOT NULL,
  `dosage` DECIMAL(3,1) UNSIGNED NOT NULL,
  `dosefrequency_per_day` INT UNSIGNED NOT NULL,
  `duration_days` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`prescription_serialno`, `generic_name`),
  CONSTRAINT `pserialno_FK_pmedicine`
    FOREIGN KEY (`prescription_serialno`)
    REFERENCES `u400425564_ccinfo208db`.`prescription` (`prescription_serialno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `genericname_FK_pmedicine`
    FOREIGN KEY (`generic_name`)
    REFERENCES `u400425564_ccinfo208db`.`ref_medicine` (`generic_name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`ref_symptoms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`ref_symptoms` (
  `symptom` VARCHAR(45) NOT NULL,
  `symptomType` ENUM('Cardinal', 'Additional') NOT NULL,
  PRIMARY KEY (`symptom`),
  UNIQUE INDEX `symptom_UNIQUE` (`symptom` ASC) VISIBLE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`healthassessment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`healthassessment` (
  `healthassessmentID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `weight_kg` DECIMAL(5,2) UNSIGNED NOT NULL,
  `height_cm` DECIMAL(5,2) UNSIGNED NOT NULL,
  `systolic_bp` DECIMAL(3,0) UNSIGNED NOT NULL,
  `diastolic_bp` DECIMAL(3,0) UNSIGNED NOT NULL,
  `body_temp` DECIMAL(3,1) UNSIGNED NOT NULL,
  PRIMARY KEY (`healthassessmentID`),
  UNIQUE INDEX `healthassessmentID_UNIQUE` (`healthassessmentID` ASC) VISIBLE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`ha_symptoms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`ha_symptoms` (
  `healthassessmentID` INT UNSIGNED NOT NULL,
  `symptom` VARCHAR(45) NOT NULL,
  `symptom_duration_2_weeks` ENUM('Y', 'N') NOT NULL,
  PRIMARY KEY (`healthassessmentID`, `symptom`),
  CONSTRAINT `symptomName_FK_pchasymptom`
    FOREIGN KEY (`symptom`)
    REFERENCES `u400425564_ccinfo208db`.`ref_symptoms` (`symptom`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `healthassessment_FK_hasymptoms`
    FOREIGN KEY (`healthassessmentID`)
    REFERENCES `u400425564_ccinfo208db`.`healthassessment` (`healthassessmentID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`ref_test_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`ref_test_type` (
  `test_typeID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `test_type` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`test_typeID`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`labresult`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`labresult` (
  `resultID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `healthassessmentID` INT UNSIGNED NOT NULL,
  `medicalcenterID` INT UNSIGNED NOT NULL,
  `test_type` INT UNSIGNED NOT NULL,
  `test_date` DATE NOT NULL,
  `test_result` ENUM('Positive', 'Negative', 'Indeterminate') NOT NULL,
  `comments` TEXT(255) NULL,
  PRIMARY KEY (`resultID`),
  UNIQUE INDEX `resultID_UNIQUE` (`resultID` ASC) VISIBLE,
  CONSTRAINT `medicalcenter_FK_cclabresult`
    FOREIGN KEY (`medicalcenterID`)
    REFERENCES `u400425564_ccinfo208db`.`medicalcenter` (`medicalcenterID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `healthassessment_FK_labresult`
    FOREIGN KEY (`healthassessmentID`)
    REFERENCES `u400425564_ccinfo208db`.`healthassessment` (`healthassessmentID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `testtype_FK_labresult`
    FOREIGN KEY (`test_type`)
    REFERENCES `u400425564_ccinfo208db`.`ref_test_type` (`test_typeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`pc_healthassessment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`pc_healthassessment` (
  `patientcaseno` INT UNSIGNED NOT NULL,
  `healthassessmentID` INT UNSIGNED NOT NULL,
  `needs_prescription` ENUM('Y', 'N') NOT NULL,
  PRIMARY KEY (`patientcaseno`, `healthassessmentID`),
  CONSTRAINT `pcno_FK_pcha`
    FOREIGN KEY (`patientcaseno`)
    REFERENCES `u400425564_ccinfo208db`.`patient_case` (`patientcaseno`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `haID_FK_pcha`
    FOREIGN KEY (`healthassessmentID`)
    REFERENCES `u400425564_ccinfo208db`.`healthassessment` (`healthassessmentID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`cc_healthassessment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`cc_healthassessment` (
  `closecontactID` INT UNSIGNED NOT NULL,
  `healthassessmentID` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`closecontactID`, `healthassessmentID`),
  CONSTRAINT `ccID_FK_ccha`
    FOREIGN KEY (`closecontactID`)
    REFERENCES `u400425564_ccinfo208db`.`closecontact` (`closecontactID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `haID_FK_ccha`
    FOREIGN KEY (`healthassessmentID`)
    REFERENCES `u400425564_ccinfo208db`.`healthassessment` (`healthassessmentID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`guardian`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`guardian` (
  `guardianID` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `lastname` VARCHAR(45) NOT NULL,
  `firstname` VARCHAR(45) NOT NULL,
  `middlename` VARCHAR(45) NOT NULL,
  `birthdate` DATE NOT NULL,
  `sex` ENUM('M', 'F') NOT NULL,
  `contactno` VARCHAR(15) NOT NULL,
  `email` VARCHAR(255) NULL,
  `relationship_to_patient` ENUM('Father', 'Mother', 'Legal Guardian', 'Relative', 'Other') NOT NULL,
  `is_emergency_contact` ENUM('Y', 'N') NOT NULL,
  PRIMARY KEY (`guardianID`),
  UNIQUE INDEX `guardianID_UNIQUE` (`guardianID` ASC) VISIBLE)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`patient_guardian`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`patient_guardian` (
  `patientID` INT UNSIGNED NOT NULL,
  `guardianID` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`patientID`, `guardianID`),
  CONSTRAINT `pID_FK_patientguardian`
    FOREIGN KEY (`patientID`)
    REFERENCES `u400425564_ccinfo208db`.`patient` (`patientID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `gID_FK_patientguardian`
    FOREIGN KEY (`guardianID`)
    REFERENCES `u400425564_ccinfo208db`.`guardian` (`guardianID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `u400425564_ccinfo208db`.`cc_guardian`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`cc_guardian` (
  `closecontactID` INT UNSIGNED NOT NULL,
  `guardianID` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`closecontactID`, `guardianID`),
  CONSTRAINT `ccID_FK_ccguardian`
    FOREIGN KEY (`closecontactID`)
    REFERENCES `u400425564_ccinfo208db`.`closecontact` (`closecontactID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `gID_FK_ccguardian`
    FOREIGN KEY (`guardianID`)
    REFERENCES `u400425564_ccinfo208db`.`guardian` (`guardianID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
USE `u400425564_ccinfo208db` ;

-- -----------------------------------------------------
-- Placeholder table for view `u400425564_ccinfo208db`.`patientcoredata_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`patientcoredata_view` (`patientID` INT, `patientfullname` INT, `birthday` INT, `sex` INT, `is_bcg_vaccinated` INT, `patient_comorbidities` INT, `patientaddress` INT, `closecontactID` INT, `is_elevated_to_patient` INT);
SHOW WARNINGS;

-- -----------------------------------------------------
-- Placeholder table for view `u400425564_ccinfo208db`.`patientguardians_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `u400425564_ccinfo208db`.`patientguardians_view` (`patientID` INT, `guardianID` INT, `guardian_fullname` INT, `birthdate` INT, `sex` INT, `contactno` INT, `email` INT, `relationship_to_patient` INT, `is_emergency_contact` INT);
SHOW WARNINGS;

-- -----------------------------------------------------
-- View `u400425564_ccinfo208db`.`patientcoredata_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `u400425564_ccinfo208db`.`patientcoredata_view`;
SHOW WARNINGS;
USE `u400425564_ccinfo208db`;
CREATE  OR REPLACE VIEW `patientcoredata_view` AS
SELECT		p.patientID, CONCAT(p.lastname, ", ", p.firstname, " ", p.middlename) AS patientfullname, p.birthday, p.sex, p.is_bcg_vaccinated,
			GROUP_CONCAT(r_c.comorbidity ORDER BY r_c.comorbidity SEPARATOR ', ') AS patient_comorbidities,
			CONCAT(ca.ca_houseno, ", ", ca.ca_subdivision, ", ", ca.ca_streetname, ", ", "Brgy. ", b.barangayName) AS patientaddress,
            cc.closecontactID, cc.is_elevated_to_patient
FROM 		patient p	LEFT JOIN 	patient_comorbidity p_c 	ON 	p.patientID = p_c.patientID
						LEFT JOIN 	ref_comorbidity r_c ON p_c.comorbidityID = r_c.comorbidityID
						LEFT JOIN 	currentaddress ca ON p.addressID = ca.addressID
						LEFT JOIN 	barangay b ON ca.ca_barangay = b.barangayID
                        LEFT JOIN closecontact cc ON p.closecontactcaseID = cc.closecontactID
GROUP BY	p.patientID, p.lastname, p.firstname, p.middlename, p.birthday, p.sex, p.is_bcg_vaccinated,
			ca.ca_houseno, ca.ca_subdivision, ca.ca_streetname, 
            b.barangayName, 
            cc.closecontactID, cc.is_elevated_to_patient
ORDER BY 	p.patientID;
SHOW WARNINGS;

-- -----------------------------------------------------
-- View `u400425564_ccinfo208db`.`patientguardians_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `u400425564_ccinfo208db`.`patientguardians_view`;
SHOW WARNINGS;
USE `u400425564_ccinfo208db`;
CREATE  OR REPLACE VIEW `patientguardians_view` AS
SELECT 		p.patientID, 
			g.guardianID, CONCAT(g.lastname, ", ", g.firstname, " ", g.middlename) AS guardian_fullname, g.birthdate, g.sex, g.contactno, g.email, g.relationship_to_patient, g.is_emergency_contact
FROM 		patient p 	JOIN 	patient_guardian p_g 	ON 	p.patientID = p_g.patientID
						JOIN 	guardian g ON p_g.guardianID = g.guardianID
ORDER BY patientID ASC, guardianID ASC, is_emergency_contact DESC;
SHOW WARNINGS;
USE `u400425564_ccinfo208db`;

DELIMITER $$
SHOW WARNINGS$$
USE `u400425564_ccinfo208db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `u400425564_ccinfo208db`.`patient_case_BEFORE_UPDATE` 
BEFORE UPDATE ON `patient_case` 
FOR EACH ROW
BEGIN
    -- Check if the case_status is updated to a "closed" or "cured" status
    IF NEW.case_status IN ('Cured', 'Closed - Died', 'Closed - Lost to Followup', 'Closed - Transferred') THEN
        -- Update the toBePrescribed field to 'F'
        SET NEW.toBePrescribed = 'F';
    END IF;
END;$$

SHOW WARNINGS$$
SHOW WARNINGS$$
USE `u400425564_ccinfo208db`$$
CREATE DEFINER = CURRENT_USER TRIGGER `u400425564_ccinfo208db`.`prescription_AFTER_INSERT_update_toBePrescribed` 
AFTER INSERT ON `prescription` 
FOR EACH ROW
BEGIN
	UPDATE patient_case
    SET toBePrescribed = 'F'
    WHERE patientcaseno = NEW.caseno;
END$$

SHOW WARNINGS$$

DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
