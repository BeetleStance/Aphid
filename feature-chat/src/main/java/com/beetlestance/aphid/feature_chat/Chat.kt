package com.beetlestance.aphid.feature_chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focusObserver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.node.Ref
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun Chat(
    paddingValues: PaddingValues
) {
    Surface {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            EmptyChat(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .weight(1f)
            )

            Spacer(modifier = Modifier.preferredHeight(16.dp))

            ChatInput(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                onQuerySubmit = {}
            )
        }
    }
}

@Composable
fun EmptyChat(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.7f)
                .weight(1f)
        ) {
            Image(
                asset = vectorResource(id = R.drawable.ic_chat_empty),
                modifier = Modifier
                    .padding(top = 32.dp)
                    .align(Alignment.TopCenter)
            )
        }

        EmptyChatHint(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            elevation = 4.dp
        )
    }
}

@Composable
fun EmptyChatHint(
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    shape: Shape = RoundedCornerShape(
        topLeftPercent = 0,
        bottomLeftPercent = 50,
        topRightPercent = 50,
        bottomRightPercent = 50
    )
) {
    Surface(
        modifier = modifier,
        elevation = elevation,
        shape = shape
    ) {
        Row {
            Image(
                modifier = Modifier.padding(16.dp)
                    .size(48.dp),
                asset = vectorResource(id = R.drawable.ic_chat_bot),
                alignment = Alignment.Center
            )

            Column(
                modifier = Modifier.padding(vertical = 16.dp),
            ) {
                Text(
                    text = "Have a delicious question?",
                    style = MaterialTheme.typography.h6
                )

                Text(
                    text = "try \" Calories in Brown Bread\"",
                    fontWeight = FontWeight.ExtraLight
                )
            }
        }
    }
}

@OptIn(ExperimentalFocus::class)
@Composable
private fun ChatInput(
    state: InputState = rememberInputState(),
    modifier: Modifier = Modifier,
    onQuerySubmit: (String) -> Unit
) {
    // reference for the keyboard
    // to be used for hiding key board
    val keyboardController: Ref<SoftwareKeyboardController> = remember { Ref() }

    Surface(
        shape = RoundedCornerShape(
            topLeftPercent = 50,
            topRightPercent = 50,
            bottomLeftPercent = 50,
            bottomRightPercent = 0
        ),
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .preferredHeight(56.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            alignment = Alignment.Center

        ) {
            if (state.query.text.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .wrapContentSize()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Left,
                        text = "Type here...",
                        color = Color.Gray
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                BasicTextField(
                    value = state.query,
                    onValueChange = {
                        state.query = it
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    onImeActionPerformed = {
                        // Change: Find out why state clear is not working here
                        // Theory: Composable is recomposing which is resulting in state side effects
                        keyboardController.value?.hideSoftwareKeyboard()
                    },
                    onTextInputStarted = {
                        keyboardController.value = it
                    },
                    modifier = Modifier
                        .weight(1f)
                )

                Icon(
                    asset = vectorResource(id = R.drawable.ic_chat_input_send),
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = {
                            state.clear()
                        })
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun rememberInputState(
    query: TextFieldValue = TextFieldValue("")
): InputState {
    return remember {
        InputState(
            query = query
        )
    }
}

@Stable
class InputState(
    query: TextFieldValue
) {
    var query by mutableStateOf(query)

    @Stable
    fun clear() {
        query = TextFieldValue("")
    }
}

@Preview
@Composable
fun ChatSection() {
    Chat(paddingValues = PaddingValues(0.dp))
}