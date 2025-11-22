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

-- Volcando estructura para tabla bibliotecasma.lector
CREATE TABLE IF NOT EXISTS `lector` (
  `LectorID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombres` varchar(100) NOT NULL,
  `Apellidos` varchar(100) NOT NULL,
  `DNI` varchar(20) NOT NULL,
  `Direccion` varchar(200) DEFAULT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`LectorID`),
  UNIQUE KEY `DNI` (`DNI`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.lector: ~2 rows (aproximadamente)
INSERT INTO `lector` (`LectorID`, `Nombres`, `Apellidos`, `DNI`, `Direccion`, `Telefono`) VALUES
	(1, 'Sofía', 'Quispe Ramos', '71234567', 'Av. Los Álamos 123', '987654321'),
	(2, 'Carlos', 'Mamani Flores', '78912345', 'Jr. Lima 456', '999111222');

-- Volcando estructura para tabla bibliotecasma.libro
CREATE TABLE IF NOT EXISTS `libro` (
  `LibroID` int(11) NOT NULL AUTO_INCREMENT,
  `Titulo` varchar(200) NOT NULL,
  `Autor` varchar(150) NOT NULL,
  `Editorial` varchar(100) DEFAULT NULL,
  `AnioPublicacion` int(11) DEFAULT NULL,
  `Estado` enum('DISPONIBLE','PRESTADO') DEFAULT 'DISPONIBLE',
  PRIMARY KEY (`LibroID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.libro: ~3 rows (aproximadamente)
INSERT INTO `libro` (`LibroID`, `Titulo`, `Autor`, `Editorial`, `AnioPublicacion`, `Estado`) VALUES
	(1, 'Cien Años de Soledad', 'Gabriel García Márquez', 'Sudamericana', 1967, 'DISPONIBLE'),
	(2, 'La Ciudad y los Perros', 'Mario Vargas Llosa', 'Seix Barral', 1963, 'DISPONIBLE'),
	(3, 'El Principito', 'Antoine de Saint-Exupéry', 'Reynal & Hitchcock', 1943, 'DISPONIBLE');

-- Volcando estructura para tabla bibliotecasma.multa
CREATE TABLE IF NOT EXISTS `multa` (
  `MultaID` int(11) NOT NULL AUTO_INCREMENT,
  `PrestamoID` int(11) NOT NULL,
  `Monto` decimal(10,2) NOT NULL,
  `Motivo` varchar(200) DEFAULT NULL,
  `FechaRegistro` date NOT NULL,
  PRIMARY KEY (`MultaID`),
  KEY `PrestamoID` (`PrestamoID`),
  CONSTRAINT `multa_ibfk_1` FOREIGN KEY (`PrestamoID`) REFERENCES `prestamo` (`PrestamoID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.multa: ~1 rows (aproximadamente)
INSERT INTO `multa` (`MultaID`, `PrestamoID`, `Monto`, `Motivo`, `FechaRegistro`) VALUES
	(1, 1, 5.00, 'Retraso de entrega', '2025-01-20');

-- Volcando estructura para tabla bibliotecasma.prestamo
CREATE TABLE IF NOT EXISTS `prestamo` (
  `PrestamoID` int(11) NOT NULL AUTO_INCREMENT,
  `LectorID` int(11) NOT NULL,
  `LibroID` int(11) NOT NULL,
  `FechaPrestamo` date NOT NULL,
  `FechaDevolucion` date DEFAULT NULL,
  `Estado` enum('PRESTADO','DEVUELTO','ATRASADO') DEFAULT 'PRESTADO',
  PRIMARY KEY (`PrestamoID`),
  KEY `LectorID` (`LectorID`),
  KEY `LibroID` (`LibroID`),
  CONSTRAINT `prestamo_ibfk_1` FOREIGN KEY (`LectorID`) REFERENCES `lector` (`LectorID`),
  CONSTRAINT `prestamo_ibfk_2` FOREIGN KEY (`LibroID`) REFERENCES `libro` (`LibroID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.prestamo: ~1 rows (aproximadamente)
INSERT INTO `prestamo` (`PrestamoID`, `LectorID`, `LibroID`, `FechaPrestamo`, `FechaDevolucion`, `Estado`) VALUES
	(1, 1, 3, '2025-01-10', NULL, 'PRESTADO');

-- Volcando estructura para tabla bibliotecasma.usuario
CREATE TABLE IF NOT EXISTS `usuario` (
  `UsuarioID` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(200) NOT NULL,
  `Rol` enum('ADMIN','LECTOR') NOT NULL,
  `LectorID` int(11) DEFAULT NULL,
  PRIMARY KEY (`UsuarioID`),
  UNIQUE KEY `Username` (`Username`),
  KEY `LectorID` (`LectorID`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`LectorID`) REFERENCES `lector` (`LectorID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla bibliotecasma.usuario: ~2 rows (aproximadamente)
INSERT INTO `usuario` (`UsuarioID`, `Username`, `Password`, `Rol`, `LectorID`) VALUES
	(1, 'admin', '123', 'ADMIN', NULL),
	(2, 'sofia', '123', 'LECTOR', 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
