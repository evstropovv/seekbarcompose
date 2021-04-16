# Seekbar Compose
Seekbar sample on Jetpack Compose 1.0.0-beta04

## About
Written witch canvas on Jetpack Compose

<img src="https://github.com/evstropovv/seekbarcompose/blob/main/photo/c741decc-926f-4c61-a0a7-59cc95861bee.jpeg" width="192">


      @Composable
      fun MainScreen(){

         val position = MutableStateFlow(10L)
         val duration = MutableStateFlow(100L)

          Seekbar(
                position = position.collectAsState(),
                duration = duration.collectAsState(),
                onNewProgress = { },
                onDragStart = { },
                onDragEnd = {
                    position.value = it
                })
      }
