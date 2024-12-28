package com.example.madcamp_week1.data

import com.example.madcamp_week1.model.RegionSeats
import com.example.madcamp_week1.model.PartySeats

object RegionSeatData {
    val regionSeatsList = listOf(
        RegionSeats(
            region = "서울",
            parties = listOf(
                PartySeats("더불어민주당", 37),
                PartySeats("국민의힘", 11)
            )
        ),
        RegionSeats(
            region = "부산",
            parties = listOf(
                PartySeats("국민의힘", 17),
                PartySeats("더불어민주당", 1)
            )
        ),
        RegionSeats(
            region = "대구",
            parties = listOf(
                PartySeats("국민의힘", 12),
                PartySeats("더불어민주당", 0)
            )
        ),
        RegionSeats(
            region = "인천",
            parties = listOf(
                PartySeats("더불어민주당", 12),
                PartySeats("국민의힘", 2)
            )
        ),
        RegionSeats(
            region = "광주",
            parties = listOf(
                PartySeats("더불어민주당", 8),
                PartySeats("국민의힘", 0)
            )
        ),
        RegionSeats(
            region = "대전",
            parties = listOf(
                PartySeats("더불어민주당", 7),
                PartySeats("국민의힘", 0)
            )
        ),
        RegionSeats(
            region = "울산",
            parties = listOf(
                PartySeats("국민의힘", 4),
                PartySeats("더불어민주당", 1),
                PartySeats("진보당", 1)
            )
        ),
        RegionSeats(
            region = "경기",
            parties = listOf(
                PartySeats("더불어민주당", 53),
                PartySeats("국민의힘", 6),
                PartySeats("개혁신당", 1)
            )
        ),
        RegionSeats(
            region = "강원",
            parties = listOf(
                PartySeats("국민의힘", 6),
                PartySeats("더불어민주당", 2)
            )
        ),
        RegionSeats(
            region = "충북",
            parties = listOf(
                PartySeats("더불어민주당", 5),
                PartySeats("국민의힘", 3)
            )
        ),
        RegionSeats(
            region = "충남",
            parties = listOf(
                PartySeats("더불어민주당", 8),
                PartySeats("국민의힘", 3)
            )
        ),
        RegionSeats(
            region = "전북",
            parties = listOf(
                PartySeats("더불어민주당", 10),
                PartySeats("국민의힘", 0)
            )
        ),
        RegionSeats(
            region = "전남",
            parties = listOf(
                PartySeats("더불어민주당", 10),
                PartySeats("국민의힘", 0)
            )
        ),
        RegionSeats(
            region = "경북",
            parties = listOf(
                PartySeats("국민의힘", 13),
                PartySeats("더불어민주당", 0)
            )
        ),
        RegionSeats(
            region = "경남",
            parties = listOf(
                PartySeats("국민의힘", 13),
                PartySeats("더불어민주당", 3)
            )
        ),
        RegionSeats(
            region = "제주",
            parties = listOf(
                PartySeats("더불어민주당", 3),
                PartySeats("국민의힘", 0)
            )
        )
    )
}
