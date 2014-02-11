package net.pterodactylus.util.tag.id3.v1;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * List of ID3v1 genres, as defined by the specs and Winamp.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Genre {

	private static final Map<Integer, String> genresByNumber = new HashMap<>();
	private static final Map<String, Integer> genresByName = new HashMap<>();

	static {
		addGenre(0, "Blues");
		addGenre(1, "Classic Rock");
		addGenre(2, "Country");
		addGenre(3, "Dance");
		addGenre(4, "Disco");
		addGenre(5, "Funk");
		addGenre(6, "Grunge");
		addGenre(7, "Hip-Hop");
		addGenre(8, "Jazz");
		addGenre(9, "Metal");
		addGenre(10, "New Age");
		addGenre(11, "Oldies");
		addGenre(12, "Other");
		addGenre(13, "Pop");
		addGenre(14, "Rhythm and Blues");
		addGenre(15, "Rap");
		addGenre(16, "Reggae");
		addGenre(17, "Rock");
		addGenre(18, "Techno");
		addGenre(19, "Industrial");
		addGenre(20, "Alternative");
		addGenre(21, "Ska");
		addGenre(22, "Death Metal");
		addGenre(23, "Pranks");
		addGenre(24, "Soundtrack");
		addGenre(25, "Euro-Techno");
		addGenre(26, "Ambient");
		addGenre(27, "Trip-Hop");
		addGenre(28, "Vocal");
		addGenre(29, "Jazz & Funk");
		addGenre(30, "Fusion");
		addGenre(31, "Trance");
		addGenre(32, "Classical");
		addGenre(33, "Instrumental");
		addGenre(34, "Acid");
		addGenre(35, "House");
		addGenre(36, "Game");
		addGenre(37, "Sound Clip");
		addGenre(38, "Gospel");
		addGenre(39, "Noise");
		addGenre(40, "Alternative Rock");
		addGenre(41, "Bass");
		addGenre(42, "Soul");
		addGenre(43, "Punk");
		addGenre(44, "Space");
		addGenre(45, "Meditative");
		addGenre(46, "Instrumental Pop");
		addGenre(47, "Instrumental Rock");
		addGenre(48, "Ethnic");
		addGenre(49, "Gothic");
		addGenre(50, "Darkwave");
		addGenre(51, "Techno-Industrial");
		addGenre(52, "Electronic");
		addGenre(53, "Pop-Folk");
		addGenre(54, "Eurodance");
		addGenre(55, "Dream");
		addGenre(56, "Southern Rock");
		addGenre(57, "Comedy");
		addGenre(58, "Cult");
		addGenre(59, "Gangsta");
		addGenre(60, "Top 40");
		addGenre(61, "Christian Rap");
		addGenre(62, "Pop/Funk");
		addGenre(63, "Jungle");
		addGenre(64, "Native US");
		addGenre(65, "Cabaret");
		addGenre(66, "New Wave");
		addGenre(67, "Psychedelic");
		addGenre(68, "Rave");
		addGenre(69, "Showtunes");
		addGenre(70, "Trailer");
		addGenre(71, "Lo-Fi");
		addGenre(72, "Tribal");
		addGenre(73, "Acid Punk");
		addGenre(74, "Acid Jazz");
		addGenre(75, "Polka");
		addGenre(76, "Retro");
		addGenre(77, "Musical");
		addGenre(78, "Rock & Roll");
		addGenre(79, "Hard Rock");
		addGenre(80, "Folk");
		addGenre(81, "Folk-Rock");
		addGenre(82, "National Folk");
		addGenre(83, "Swing");
		addGenre(84, "Fast Fusion");
		addGenre(85, "Bebop");
		addGenre(86, "Latin");
		addGenre(87, "Revival");
		addGenre(88, "Celtic");
		addGenre(89, "Bluegrass");
		addGenre(90, "Avantgarde");
		addGenre(91, "Gothic Rock");
		addGenre(92, "Progressive Rock");
		addGenre(93, "Psychedelic Rock");
		addGenre(94, "Symphonic Rock");
		addGenre(95, "Slow Rock");
		addGenre(96, "Big Band");
		addGenre(97, "Chorus");
		addGenre(98, "Easy Listening");
		addGenre(99, "Acoustic");
		addGenre(100, "Humour");
		addGenre(101, "Speech");
		addGenre(102, "Chanson");
		addGenre(103, "Opera");
		addGenre(104, "Chamber Music");
		addGenre(105, "Sonata");
		addGenre(106, "Symphony");
		addGenre(107, "Booty Bass");
		addGenre(108, "Primus");
		addGenre(109, "Porn Groove");
		addGenre(110, "Satire");
		addGenre(111, "Slow Jam");
		addGenre(112, "Club");
		addGenre(113, "Tango");
		addGenre(114, "Samba");
		addGenre(115, "Folklore");
		addGenre(116, "Ballad");
		addGenre(117, "Power Ballad");
		addGenre(118, "Rhythmic Soul");
		addGenre(119, "Freestyle");
		addGenre(120, "Duet");
		addGenre(121, "Punk Rock");
		addGenre(122, "Drum Solo");
		addGenre(123, "A capella");
		addGenre(124, "Euro-House");
		addGenre(125, "Dance Hall");
		addGenre(126, "Goa");
		addGenre(127, "Drum & Bass");
		addGenre(128, "Club-House");
		addGenre(129, "Hardcore Techno");
		addGenre(130, "Terror");
		addGenre(131, "Indie");
		addGenre(132, "BritPop");
		addGenre(133, "Negerpunk");
		addGenre(134, "Polsk Punk");
		addGenre(135, "Beat");
		addGenre(136, "Christian Gangsta Rap");
		addGenre(137, "Heavy Metal");
		addGenre(138, "Black Metal");
		addGenre(139, "Crossover");
		addGenre(140, "Contemporary Christian");
		addGenre(141, "Christian Rock");
		addGenre(142, "Merengue");
		addGenre(143, "Salsa");
		addGenre(144, "Thrash Metal");
		addGenre(145, "Anime");
		addGenre(146, "Jpop");
		addGenre(147, "Synthpop");
		addGenre(148, "Abstract");
		addGenre(149, "Art Rock");
		addGenre(150, "Baroque");
		addGenre(151, "Bhangra");
		addGenre(152, "Big Beat");
		addGenre(153, "Breakbeat");
		addGenre(154, "Chillout");
		addGenre(155, "Downtempo");
		addGenre(156, "Dub");
		addGenre(157, "EBM");
		addGenre(158, "Eclectic");
		addGenre(159, "Electro");
		addGenre(160, "Electroclash");
		addGenre(161, "Emo");
		addGenre(162, "Experimental");
		addGenre(163, "Garage");
		addGenre(164, "Global");
		addGenre(165, "IDM");
		addGenre(166, "Illbient");
		addGenre(167, "Industro-Goth");
		addGenre(168, "Jam Band");
		addGenre(169, "Krautrock");
		addGenre(170, "Leftfield");
		addGenre(171, "Lounge");
		addGenre(172, "Math Rock");
		addGenre(173, "New Romantic");
		addGenre(174, "Nu-Breakz");
		addGenre(175, "Post-Punk");
		addGenre(176, "Post-Rock");
		addGenre(177, "Psytrance");
		addGenre(178, "Shoegaze");
		addGenre(179, "Space Rock");
		addGenre(180, "Trop Rock");
		addGenre(181, "World Music");
		addGenre(182, "Neoclassical");
		addGenre(183, "Audiobook");
		addGenre(184, "Audio Theatre");
		addGenre(185, "Neue Deutsche Welle");
		addGenre(186, "Podcast");
		addGenre(187, "Indie Rock");
		addGenre(188, "G-Funk");
		addGenre(189, "Dubstep");
		addGenre(190, "Garage Rock");
		addGenre(191, "Psybient");
	}

	public static Optional<String> getName(int number) {
		if ((number < 0) || (number >= genresByNumber.size())) {
			return empty();
		}
		return of(genresByNumber.get(number));
	}

	public static Optional<Integer> getNumber(String name) {
		if (!genresByName.containsKey(name.toLowerCase())) {
			return empty();
		}
		return of(genresByName.get(name.toLowerCase()));
	}

	private static void addGenre(int number, String name) {
		genresByNumber.put(number, name);
		genresByName.put(name.toLowerCase(), number);
	}

}
