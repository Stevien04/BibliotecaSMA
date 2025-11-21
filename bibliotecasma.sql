-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.32-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para bibliotecasma
CREATE DATABASE IF NOT EXISTS `bibliotecasma` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `bibliotecasma`;

-- Volcando estructura para tabla bibliotecasma.detalleprestamo
CREATE TABLE IF NOT EXISTS `detalleprestamo` (
  `DetalleID` int(11) NOT NULL AUTO_INCREMENT,
  `PrestamoID` int(11) NOT NULL,
  `LibroID` int(11) NOT NULL,
  `Cantidad` int(11) NOT NULL CHECK (`Cantidad` > 0),
  PRIMARY KEY (`DetalleID`),
  KEY `PrestamoID` (`PrestamoID`),
  KEY `LibroID` (`LibroID`),
  CONSTRAINT `detalleprestamo_ibfk_1` FOREIGN KEY (`PrestamoID`) REFERENCES `prestamo` (`PrestamoID`),
  CONSTRAINT `detalleprestamo_ibfk_2` FOREIGN KEY (`LibroID`) REFERENCES `libro` (`LibroID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.detalleprestamo: ~2 rows (aproximadamente)
INSERT INTO `detalleprestamo` (`DetalleID`, `PrestamoID`, `LibroID`, `Cantidad`) VALUES
	(1, 1, 1, 1),
	(2, 1, 3, 1);

-- Volcando estructura para tabla bibliotecasma.devolucion
CREATE TABLE IF NOT EXISTS `devolucion` (
  `DevolucionID` int(11) NOT NULL AUTO_INCREMENT,
  `PrestamoID` int(11) NOT NULL,
  `FechaDevolucion` date NOT NULL,
  `Observacion` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`DevolucionID`),
  KEY `PrestamoID` (`PrestamoID`),
  CONSTRAINT `devolucion_ibfk_1` FOREIGN KEY (`PrestamoID`) REFERENCES `prestamo` (`PrestamoID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.devolucion: ~1 rows (aproximadamente)
INSERT INTO `devolucion` (`DevolucionID`, `PrestamoID`, `FechaDevolucion`, `Observacion`) VALUES
	(1, 1, '2025-12-05', 'Devuelto tarde');

-- Volcando estructura para tabla bibliotecasma.lector
CREATE TABLE IF NOT EXISTS `lector` (
  `LectorID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(100) NOT NULL,
  `Apellido` varchar(100) NOT NULL,
  `DNI` varchar(15) NOT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  `Direccion` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`LectorID`),
  UNIQUE KEY `DNI` (`DNI`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.lector: ~2 rows (aproximadamente)
INSERT INTO `lector` (`LectorID`, `Nombre`, `Apellido`, `DNI`, `Telefono`, `Direccion`) VALUES
	(1, 'Sofia', 'Mendoza', '12345678', '987654321', 'Av. Perú 123'),
	(2, 'Luis', 'Quispe', '87654321', '912345678', 'Calle Lima 456');

-- Volcando estructura para tabla bibliotecasma.libro
CREATE TABLE IF NOT EXISTS `libro` (
  `LibroID` int(11) NOT NULL AUTO_INCREMENT,
  `Titulo` varchar(200) NOT NULL,
  `Autor` varchar(150) DEFAULT NULL,
  `Editorial` varchar(150) DEFAULT NULL,
  `AnioPublicacion` int(11) DEFAULT NULL,
  `Categoria` varchar(100) DEFAULT NULL,
  `Stock` int(11) NOT NULL,
  PRIMARY KEY (`LibroID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.libro: ~3 rows (aproximadamente)
INSERT INTO `libro` (`LibroID`, `Titulo`, `Autor`, `Editorial`, `AnioPublicacion`, `Categoria`, `Stock`) VALUES
	(1, 'El Principito', 'Antoine de Saint-Exupéry', 'Emecé', 1943, 'Ficción', 5),
	(2, 'Cien Años de Soledad', 'Gabriel García Márquez', 'Sudamericana', 1967, 'Realismo Mágico', 3),
	(3, 'Hamlet', 'William Shakespeare', 'Penguin', 1603, 'Drama', 4);

-- Volcando estructura para tabla bibliotecasma.multa
CREATE TABLE IF NOT EXISTS `multa` (
  `MultaID` int(11) NOT NULL AUTO_INCREMENT,
  `PrestamoID` int(11) NOT NULL,
  `DiasRetraso` int(11) NOT NULL,
  `Monto` decimal(10,2) NOT NULL,
  `FechaGenerada` date NOT NULL,
  PRIMARY KEY (`MultaID`),
  KEY `PrestamoID` (`PrestamoID`),
  CONSTRAINT `multa_ibfk_1` FOREIGN KEY (`PrestamoID`) REFERENCES `prestamo` (`PrestamoID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.multa: ~1 rows (aproximadamente)
INSERT INTO `multa` (`MultaID`, `PrestamoID`, `DiasRetraso`, `Monto`, `FechaGenerada`) VALUES
	(1, 1, 8, 12.00, '2025-11-21');

-- Volcando estructura para tabla bibliotecasma.prestamo
CREATE TABLE IF NOT EXISTS `prestamo` (
  `PrestamoID` int(11) NOT NULL AUTO_INCREMENT,
  `LectorID` int(11) NOT NULL,
  `FechaPrestamo` date NOT NULL,
  `FechaMaximaDevolucion` date NOT NULL,
  `Estado` varchar(20) NOT NULL DEFAULT 'Prestado',
  PRIMARY KEY (`PrestamoID`),
  KEY `LectorID` (`LectorID`),
  CONSTRAINT `prestamo_ibfk_1` FOREIGN KEY (`LectorID`) REFERENCES `lector` (`LectorID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.prestamo: ~1 rows (aproximadamente)
INSERT INTO `prestamo` (`PrestamoID`, `LectorID`, `FechaPrestamo`, `FechaMaximaDevolucion`, `Estado`) VALUES
	(1, 1, '2025-11-20', '2025-11-27', 'Prestado');

-- Volcando estructura para disparador bibliotecasma.trg_AumentarStock
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_ZERO_IN_DATE,NO_ZERO_DATE,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER trg_AumentarStock
AFTER INSERT ON Devolucion
FOR EACH ROW
BEGIN
    UPDATE Libro l
    JOIN DetallePrestamo dp ON l.LibroID = dp.LibroID
    SET l.Stock = l.Stock + dp.Cantidad
    WHERE dp.PrestamoID = NEW.PrestamoID;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador bibliotecasma.trg_GenerarMultaPorRetraso
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_ZERO_IN_DATE,NO_ZERO_DATE,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER trg_GenerarMultaPorRetraso
AFTER INSERT ON Devolucion
FOR EACH ROW
BEGIN
    DECLARE fechaLimite DATE;
    DECLARE diasRetraso INT;
    DECLARE monto DECIMAL(10,2);

    SELECT FechaMaximaDevolucion INTO fechaLimite
    FROM Prestamo
    WHERE PrestamoID = NEW.PrestamoID;

    SET diasRetraso = DATEDIFF(NEW.FechaDevolucion, fechaLimite);

    IF diasRetraso > 0 THEN
        SET monto = diasRetraso * 1.50;

        INSERT INTO Multa (PrestamoID, DiasRetraso, Monto, FechaGenerada)
        VALUES (NEW.PrestamoID, diasRetraso, monto, CURDATE());
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador bibliotecasma.trg_ReducirStock
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_ZERO_IN_DATE,NO_ZERO_DATE,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER trg_ReducirStock
AFTER INSERT ON DetallePrestamo
FOR EACH ROW
BEGIN
    UPDATE Libro
    SET Stock = Stock - NEW.Cantidad
    WHERE LibroID = NEW.LibroID;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
