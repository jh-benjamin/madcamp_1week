## 국회ON(AssemblyON)

### OutLine
---
🇰🇷🇰🇷🇰🇷 **국회ON**은 국회에 대한 정보를 쉽고 편하게 확인할 수 있게 도와주는 플랫폼이에요. 🇰🇷🇰🇷🇰🇷
- **이재명**, **권성동**과 같은 국회의원의 연락처와 사무실을 알 수 있어요.
- 국회의원 300명의 사진을 한 곳에서 모아 볼 수 있어요.
- **더불어민주당**, **국민의힘** 등 정당별 국회의원 의석 현황을 살펴볼 수 있어요.
  
### Tech Stack
---
**Front-End** : Kotlin

**IDE** : Android Studio

## Details about Tech

### Tab1 (HomeFragment)

- 22대 국회의원 정보 리스트를 확인할 수 있어요.
- 이름 혹은 정당을 통해 검색할 수 있어요.
    - SearchView를 활용하여 구현했어요.
    - 이름뿐만 아니라 정당 기준으로도 필터링을 적용하여 해당되는 의원 리스트를 반환해요.
    - 즐겨찾기 기능과 연동하여 검색할 수 있어요.
- 즐겨찾기 기능을 이용할 수 있어요.
    - 즐겨찾기 아이콘을 클릭하면 즐겨찾기 의원으로 설정돼요.
    - 즐겨찾기 의원은 맨 위에 있어요.
    - 검색 옆에 있는 별 아이콘을 통해 즐겨찾기한 의원만 볼 수 있어요.
- 전화 아이콘을 통해 사무실로 바로 전화할 수 있어요.
    - 전화 아이콘을 클릭하면 Intent.ACTION_DIAL를 실핼하게 구현했어요.
- 이미지를 클릭하여 세부 정보를 확인할 수 있어요.
    - 국회 API를 활용해 현직 국회의원의 정보를 json으로 저장했어요.
    - json 파일에서 데이터를 불러와 dialog로 띄웠어요.

### Tab2 (DashboardFragment, GalleryAdapter)

- 22대 모든 국회의원 사진을 한번에 볼 수 있어요.
    - RectclerView를 통해 국회의원의 이미지를 띄웠어요.
    - 국회의원 이미지는 Glide를 통해 인터넷에서 불러와요.
    - 1분할, 2분할, 4분할 버튼을 만들어 한 화면에 볼 수 있는 이미지의 수를 조절할 수 있어요.
- 이미지를 클릭하여 세부 정보를 확인할 수 있어요.
    - 국회 API를 활용해 현직 국회의원의 정보를 json으로 저장했어요.
    - json 파일에서 데이터를 불러와 dialog로 띄웠어요.

### Tab3 (NotificationsFragment, PartyInfoAdapter)

- 현재 및 과거의 정당별 의석 수를 확인할 수 있어요.
    - Spinner를 활용해 22대, 21대, 20대를 선택할 수 있게 구현했어요.
    - PartyInfoAdapter 클래스를 만들어 역대 국회의 정당과 그 의석 수를 받아오게 구현했어요.
    - SemiCircleGraphView 클래스를 만들어 반원을 그리는 기능을 추가했어요.
    - Animation을 통해 왼쪽에서 원이 그려지는 느낌이 나게 구현했어요.

### Etc…

- 하단바나 양 옆 스와이핑으로 화면을 전환할 수 있어요. (MainActivity)
    - BottomNavigationView와 ViewPager를 활용해서 구현했어요.
    - BottomNavigationView와 ViewPager가 변할 때마다 서로를 동기화하여 화면 전환 오류를 없앴어요.
- 아래로 스와이핑하면 하단바가 사라져요. (HomeFragment, DashboardFragment)
    - RecyclerView의 스크롤 이벤트를 감지하는 기능을 사용했어요.
    - 아래로 스크롤하면 BottomNavigationView를 가리고, 위로 올리면 다시 보이도록 설정했어요.
    - Animation 기능을 추가하여 자연스럽게 BottomNavigationView가 사라지고 나타나게 구현했어요.

### Contributers

| 이름     | 깃허브        |
|----------|--------------|
| 이재환  | [@jh-benjamin](https://github.com/jh-benjamin) |
| 최윤지  | [@yoon0701](https://github.com/yoon0701)    |
