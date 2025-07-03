-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Lug 03, 2025 alle 16:40
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
  `DISPONIBILE` tinyint(1) NOT NULL,
  `URL` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `LOCALE`
--

INSERT INTO `LOCALE` (`ID`, `NOME`, `ORA_APERTURA`, `ORA_CHIUSURA`, `INDIRIZZO`, `GIORNI_APERTURA`, `DISPONIBILE`, `URL`) VALUES
(1, 'Braci e Basilico', '09:00', '18:00', 'VIa Aldrovanni 16, Bologna', 'Lun,\r\nMar,\r\nMer,\r\nGio,\r\nVen,\r\nDom', 1, 'braciebasilico.romanellas.cloud'),
(1958374512, 'Osteria mare e bosco', '12:00', '22:00', 'Via Guglielmo Oberdan 16, Bologna', 'Lun,Mar,Mer,Gio,Ven', 1, 'osteriamareebosco.romanellas.cloud'),
(1958374513, 'Il Vicoletto Segreto', '11:00', '22:00', 'Via Edoardo Ferravilla 3, Bologna', 'Lun', 1, 'ilvicolettosegreto.romanellas.cloud'),
(1958374514, 'La Forchetta ribelle', '12:00', '22:00', 'Via Mondo 23, Bologna', 'Lun,Mar,Mer,Gio,Ven,Sab', 1, 'laforchettaribelle.romanellas.cloud'),
(1958374515, 'Cantina Fior di sale', '12:00', '22:00', 'Contrada Vazzieri 12, Campobasso', 'Lun,Mar,Mer,Gio', 1, 'cantinafiordisale.romanellaas.cloud');

-- --------------------------------------------------------

--
-- Struttura della tabella `MENU`
--

CREATE TABLE `MENU` (
  `ID` int(11) NOT NULL,
  `DESCRIZIONE` varchar(500) NOT NULL,
  `TIPO` varchar(50) NOT NULL,
  `PREZZO` int(11) NOT NULL,
  `ID_LOCALE` int(11) NOT NULL,
  `DATA` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `MENU`
--

INSERT INTO `MENU` (`ID`, `DESCRIZIONE`, `TIPO`, `PREZZO`, `ID_LOCALE`, `DATA`) VALUES
(1, 'nuovo menu', 'Pesce', 0, 1, '2025-06-13'),
(23, 'Prova 1', 'Cena', 23, 23, ''),
(116724299, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(559034578, 'è buono', 'Cena', 50, 0, ''),
(713227581, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1384498373, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1443431554, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1645620109, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1929440470, 'Daily Menu', 'Carne', 0, 1, '2025-06-13'),
(1966904463, 'Daily Menu', 'Carne', 0, 1, '2025-06-13');

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
  `ID_TRANSAZIONE` varchar(50) NOT NULL,
  `PREZZO` int(11) NOT NULL,
  `ID_MENU` int(11) NOT NULL,
  `QUANTITÀ` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `ORDINE`
--

INSERT INTO `ORDINE` (`ID_UTENTE`, `ID`, `ID_LOCALE`, `ID_SOC_C`, `ORA_CONSEGNA`, `ORA_ACQUISTO`, `ID_TRANSAZIONE`, `PREZZO`, `ID_MENU`, `QUANTITÀ`) VALUES
(1, 998387053, 1, 0, '03-07-2025 19:00', '', '', 1, 1, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `PIATTO`
--

CREATE TABLE `PIATTO` (
  `ID` int(11) NOT NULL,
  `NOME` varchar(50) NOT NULL,
  `DESCRIZIONE` varchar(256) NOT NULL,
  `PREZZO` int(11) NOT NULL,
  `MENU_ID` int(11) NOT NULL,
  `DATA` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `PIATTO`
--

INSERT INTO `PIATTO` (`ID`, `NOME`, `DESCRIZIONE`, `PREZZO`, `MENU_ID`, `DATA`) VALUES
(1, 'carbonara', '\"Jowls, Egg yolks, Pecorino Romano PDO,Black pepper\"', 8, 1, '2025-06-13'),
(23, 'carbonara', 'string', 0, 0, '2025-06-13'),
(24, 'carbonara222', 'aaaaaaaaaaaaaaaaa', 0, 0, '2025-06-13'),
(1492068332, 'carbo', 'aaaaaaaaaaaaaaaaa', 0, 0, '2025-06-13');

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
(1, 'deliveroo', 'via boznan, Ungheria', 2, 1),
(2, 'cimangiamo.romanellas.cloud', 'Via Mondo 14, Bologna', 10, 1),
(3, 'famechimica.romanellas.cloud', 'Via edoardo Ferravilla 19,Bologna', 12, 1),
(4, 'panzafly.romanellas.cloud', 'Via delle Lame 116, Bologna', 5, 1),
(5, 'toctocgnam.romanellas.cloud', 'Via Edmondo de Amicis 14, Campobasso ', 12, 1);

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
-- Indici per le tabelle `PIATTO`
--
ALTER TABLE `PIATTO`
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
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1958374516;

--
-- AUTO_INCREMENT per la tabella `MENU`
--
ALTER TABLE `MENU`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1966904464;

--
-- AUTO_INCREMENT per la tabella `ORDINE`
--
ALTER TABLE `ORDINE`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2136033153;

--
-- AUTO_INCREMENT per la tabella `PIATTO`
--
ALTER TABLE `PIATTO`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1492068333;

--
-- AUTO_INCREMENT per la tabella `SOCIETÀ_CONSEGNA`
--
ALTER TABLE `SOCIETÀ_CONSEGNA`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT per la tabella `UTENTE`
--
ALTER TABLE `UTENTE`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1170620788;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
