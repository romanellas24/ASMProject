-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Apr 23, 2025 alle 16:51
-- Versione del server: 10.4.32-MariaDB
-- Versione PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ACMEAT`
--
CREATE DATABASE IF NOT EXISTS `ACMEAT` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `ACMEAT`;

-- --------------------------------------------------------

--
-- Struttura della tabella `LOCALE`
--

CREATE TABLE `LOCALE` (
  `ID` int(11) NOT NULL,
  `NOME` varchar(50) NOT NULL,
  `ORA_APERTURA` varchar(50) NOT NULL,
  `ORA_CHIUSURA` varchar(50) NOT NULL,
  `INDIRIZZO` varchar(100) NOT NULL,
  `GIORNI_APERTURA` varchar(100) NOT NULL,
  `DISPONIBILE` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `LOCALE`
--

INSERT INTO `LOCALE` (`ID`, `NOME`, `ORA_APERTURA`, `ORA_CHIUSURA`, `INDIRIZZO`, `GIORNI_APERTURA`, `DISPONIBILE`) VALUES
(1, 'Pizzeria Primavera', '09:00', '18:00', 'VIa Aldrovanni 16, Bologna', 'Lun,\r\nMar,\r\nMer,\r\nGio,\r\nVen,\r\nDom', 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `MENU`
--

CREATE TABLE `MENU` (
  `ID` int(11) NOT NULL,
  `DESCRIZIONE` varchar(500) NOT NULL,
  `TIPO` varchar(50) NOT NULL,
  `PREZZO` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `MENU`
--

INSERT INTO `MENU` (`ID`, `DESCRIZIONE`, `TIPO`, `PREZZO`) VALUES
(1, 'il menu è buono', 'Giorno', 30);

-- --------------------------------------------------------

--
-- Struttura della tabella `ORDINE`
--

CREATE TABLE `ORDINE` (
  `ID_UTENTE` int(11) NOT NULL,
  `ID` int(11) NOT NULL,
  `ID_LOCALE` int(11) NOT NULL,
  `ID_SOC_C` int(11) NOT NULL,
  `ORA_CONSEGNA` varchar(50) NOT NULL,
  `ORA_ACQUISTO` varchar(50) NOT NULL,
  `ID_TRANSAZIONE` int(11) NOT NULL,
  `PREZZO` int(11) NOT NULL,
  `ID_MENU` int(11) NOT NULL,
  `QUANTITÀ` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `ORDINE`
--

INSERT INTO `ORDINE` (`ID_UTENTE`, `ID`, `ID_LOCALE`, `ID_SOC_C`, `ORA_CONSEGNA`, `ORA_ACQUISTO`, `ID_TRANSAZIONE`, `PREZZO`, `ID_MENU`, `QUANTITÀ`) VALUES
(0, 5, 0, 0, '09:18', '10:00', 2, 40, 0, 0),
(0, 6, 0, 0, '', '', 0, 500, 0, 0),
(0, 7, 0, 0, '', '', 0, 500, 0, 0),
(2, 9, 0, 0, '19:45', '10:00', 3, 20, 2, 1),
(6, 1602168585, 0, 0, '18:56', '17:30', 2, 20, 3, 50);

-- --------------------------------------------------------

--
-- Struttura della tabella `SOCIETÀ_CONSEGNA`
--

CREATE TABLE `SOCIETÀ_CONSEGNA` (
  `ID` int(11) NOT NULL,
  `NOME` varchar(50) NOT NULL,
  `INDIRIZZO` varchar(100) NOT NULL,
  `PREZZO` int(11) NOT NULL,
  `DISPONIBILE` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `SOCIETÀ_CONSEGNA`
--

INSERT INTO `SOCIETÀ_CONSEGNA` (`ID`, `NOME`, `INDIRIZZO`, `PREZZO`, `DISPONIBILE`) VALUES
(1, 'deliveroo', 'via boznan, Ungheria', 2, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `UTENTE`
--

CREATE TABLE `UTENTE` (
  `ID` int(11) NOT NULL,
  `INDIRIZZO` varchar(50) NOT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `PWD` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `UTENTE`
--

INSERT INTO `UTENTE` (`ID`, `INDIRIZZO`, `EMAIL`, `PWD`) VALUES
(1, 'via verdi, Campobasso CH', 'g.zemignani@studenti.unimol.it', 'ahahah'),
(4, 'prova 1', 'sssss', 'aaaaaaaaaaaaa');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `LOCALE`
--
ALTER TABLE `LOCALE`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `MENU`
--
ALTER TABLE `MENU`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `ORDINE`
--
ALTER TABLE `ORDINE`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `SOCIETÀ_CONSEGNA`
--
ALTER TABLE `SOCIETÀ_CONSEGNA`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `UTENTE`
--
ALTER TABLE `UTENTE`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `LOCALE`
--
ALTER TABLE `LOCALE`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT per la tabella `MENU`
--
ALTER TABLE `MENU`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT per la tabella `ORDINE`
--
ALTER TABLE `ORDINE`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1602168586;

--
-- AUTO_INCREMENT per la tabella `SOCIETÀ_CONSEGNA`
--
ALTER TABLE `SOCIETÀ_CONSEGNA`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT per la tabella `UTENTE`
--
ALTER TABLE `UTENTE`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
