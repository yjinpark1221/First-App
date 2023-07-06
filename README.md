# First-App

## 프로젝트 소개

2023년 몰입캠프 첫 주차 과제입니다. 

세 탭으로 이루어진 안드로이드 어플입니다. 

연락처, 갤러리, 지뢰찾기로 구성되어 있습니다. 

### 개발 환경

- Android Studio
- kotlin
- 테스트를 위해 사용한 기기
    - **Galaxy S7 (SM-G930)**
    - **Galaxy Note10 5G (SM-N970)**

### 팀 소개

2023년 몰입캠프 1분반 3조입니다. 

- 박연진([Github](https://github.com/yjinpark1221))
    - 전체적인 UI
    - Tab1. 연락처 전반적인 구현
    - Tab2. 앨범 상세 액티비티 추가
    - Tab3. 지뢰찾기 게임 동작 방식, 난이도 변경, Scoreboard 구현
- 배민성([Github](https://github.com/GuidoJoshua))
    - Tab1. 연락처 목록의 item 클릭 시 eventHandler 추가
    - Tab2. 갤러리 전반적인 구현
    - Tab3. 지뢰찾기 UI, 타이머, 남은 지뢰 개수 구현

## Tab1. 연락처

### 연락처 목록과 연락처 상세 보기

`RecyclerView`를 이용해 연락처 목록을 구현했습니다.

Item 클릭 시 연락처 상세 화면으로 이어집니다.

새 activity로 연락처 상세 화면을 띄웠습니다.

![기본 연락처 목록](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/e59b918c-7e78-4614-95da-4285bad6f4b3)


### 전화 걸기

call button(`ImageButton`)을 추가해 클릭 시 Permission 확인 후 해당 전화번호로 통화를 연결합니다.

### 연락처 검색

연락처 목록에서 검색이 가능합니다. `EditText`를 이용해 입력을 받아, 목록의 이름, 전화번호, 이메일가 검색어를 포함하는지 확인 후 해당하는 연락처를 임시로 저장해 `Adapter`에 전달합니다. 

![연락처 검색](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/92c76b8f-a920-4843-9b57-a84dd06ae68a)

### 연락처 추가와 기기로부터 import 기능

오른쪽 아래 + 버튼(`FloatingActionButton`)을 누르면 연락처를 추가할 수 있는 두 가지 옵션이 보입니다.

첫째는, 사용자의 입력을 통해, 이름, 전화번호, 이메일을 추가할 수 있습니다. 연락처 형식이 맞지 않는 경우 저장이 불가하며, SAVE(`Button`)가 disable됩니다. 이름과 전화번호는 필수이며, 이메일 형식이 맞아야합니다. 

둘째는, 기기에 저장된 전화번호를 자동으로 추가할 수 있습니다.

만약 추가하려는 이름이 기존에 있는 이름과 겹치면, 덮어쓰기 여부를 묻는 알림창(`AlertDialog`)을 띄웁니다.

![0](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/8d51d1bb-1de8-4c07-b648-0e8d158cc8aa)
![0](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/ba3d30a0-f22b-45ad-b6d5-bbcdbd24d114)
![0](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/29f2918a-11cb-4fe0-95eb-9d68902de4ed)

### 연락처 편집과 삭제

연락처 상세 화면에서 EDIT(`Button`)을 누르면 연락처를 편집할 수 있습니다. 연락처 저장 가능 여부는 연락처 추가와 동일합니다. 

DELETE(`Button`)을 누르면 연락처가 삭제되며 연락처 목록으로 돌아갑니다. 

## Tab2. 갤러리

gallery 기능

### 앨범 단위로 갤러리 보기
    
갤러리를 구성할 때, 갤러리 화면에서 앨범 단위로 접근할 수 있고, 앨범을 클릭할 경우 앨범의 세부 내용(앨범 내 사진들)을  확인할 수 있도록 구현
    
- gallery fragment를 album을 display하는 recyclerview를 display하도록 구현합니다.
- gallery fragment는 album의 대표사진과 albumname을 display합니다.

![Screenshot_20230705-165355_FirstApp](https://github.com/yjinpark1221/Madcamp-Week1/assets/138095170/1e23124a-b6a9-4ffd-aec0-54a1da793bd5)
- albumname과 imagelist로 구성된 album data class 정의합니다.
- 각 element 단위에 onclicklistener 구현하여 앨범 클릭시 해당 앨범의 이미지들을 표현하는 activity로 이동합니다.
  
### 이미지 단위로 앨범
- 갤러리에서 앨범 클릭시, 해당 앨범의 이미지 리스트 내 이미지를 표시하도록 구현합니다.
![Screenshot_20230705-165404_FirstApp](https://github.com/yjinpark1221/Madcamp-Week1/assets/138095170/75d6a0ba-7ff2-47f0-ad90-23a94c3a7a6b)
- 앨범 클릭시 새 activity 만들고, intent를 통해 해당 앨범의 정보(앨범 이름, 이미지 리스트) 전달합니다.
- 새 activity에서는 해당 앨범의 이미지의 리스트를 recyclerview를 통해 표시합니다.
- recyclerview에서 이미지를 클릭할 경우 dialog layout으로 확대된 이미지를 표시합니다.
![Screenshot_20230705-165414_FirstApp](https://github.com/yjinpark1221/Madcamp-Week1/assets/138095170/0c66a9c8-a812-4d32-b999-34a4e9873aaa)



## Tab3. 지뢰찾기 (자유 주제)

### 기본 지뢰찾기 구현

하단의 START🙂 버튼을 누르면 게임이 시작됩니다. 

![0](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/b461a118-9036-4b50-a1f5-7686d2611227)
![0](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/498a87ec-c24d-4dea-86b4-2814764be5e3)

### 모드 변경

상단의 Toggle Button을 통해서 난이도를 변경할 수 있습니다. 

![0](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/7bae9de0-a2d9-4c7b-a851-eb90c20460a8)

### 승리 시 사용자 기록 저장 & Scoreboard

승리 시 플레이어의 이름을 입력받아 기록합니다. 

언제든 우측 상단의 스코어보드 버튼을 누르면 `Dialog` 를 통해 난이도별로 최고 기록 3개를 보여줍니다.

![0](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/071146f2-6d59-42a5-8731-d0c2224d14fe)

![0](https://github.com/yjinpark1221/Madcamp-Week1/assets/67498785/957e986f-ead8-4652-9838-d31dc2ef74e2)
