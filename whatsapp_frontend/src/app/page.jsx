"use client";
import React, { useEffect, useRef, useState } from "react";
import "./globals.css";
import { useRouter } from "next/navigation";
import Profile from "../../src/app/components/Profile/Profile";
import CreateGroup from "../../src/app/components/Group/CreateGroup";
import { useDispatch, useSelector } from "react-redux";
import { currentUser, logoutAction, searchUser } from "../../src/lib/Redux/Auth/Action";
import { createChat, getUsersChat } from "../../src/lib/Redux/Chat/Action";
import { createMessage, getAllMessages } from "../../src/lib/Redux/Message/Action";
import { Client } from "@stomp/stompjs";
import ProfileSection from "../../src/app/components/HomeComponents/ProfileSection";
import SearchBar from "../../src/app/components/HomeComponents/SearchBar";
import ChatList from "../../src/app/components/HomeComponents/ChatList";
import MessageCard from "../../src/app/components/MessageCard/MessageCard";
import { AiOutlineSearch } from "react-icons/ai";
import { BsEmojiSmile, BsMicFill, BsThreeDotsVertical } from "react-icons/bs";
import { ImAttachment } from "react-icons/im";
import SockJS from "sockjs-client";

function HomePage() {
  const [querys, setQuerys] = useState("");
  const [currentChat, setCurrentChat] = useState(null);
  const [content, setContent] = useState("");
  const [isProfile, setIsProfile] = useState(false);
  const router = useRouter();
  const [isGroup, setIsGroup] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const dispatch = useDispatch();
  const { auth, chat, message } = useSelector((store) => store);
 
  const [stompClient, setStompClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [messages, setMessages] = useState([]);
  const [lastMessages, setLastMessages] = useState({});
  const messageContainerRef = useRef(null);
  const [token, setToken] = useState("");

  useEffect(() => {
    if (typeof window !== "undefined") {
      const storedToken = localStorage.getItem("token");
      setToken(storedToken);
    }
  }, []);

    // Function to get a specific cookie by name
 
    function getCookie(name) {
      if (typeof document !== "undefined") {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) {
          return parts.pop().split(";").shift();
        }
      }
      return "";
    }

    useEffect(() => {
      if (messageContainerRef.current) {
        messageContainerRef.current.scrollTop = messageContainerRef.current.scrollHeight;
      }
    }, [messages]); // This will trigger when `messages` change

  // Function to establish a WebSocket connection
  useEffect(() => {
    if (!token) return; // wait until token is set

    const client = new Client({
      // The SockJS endpoint must match your Spring config: registry.addEndpoint("/ws")...
      webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
        // If you need CSRF token, you can pass it here:
        // "X-XSRF-TOKEN": getCookie("XSRF-TOKEN"),
      },
      debug: (str) => console.log("STOMP Debug:", str),
      reconnectDelay: 5000,

      // Called when the client is connected to STOMP broker
      onConnect: () => {
        console.log("STOMP Client Connected");
        setIsConnected(true);
      },

      // Optional error handlers
      onStompError: (frame) => {
        console.error("Broker reported error: " + frame.headers["message"]);
        console.error("Additional details: " + frame.body);
      },
      onWebSocketError: (event) => {
        console.error("WebSocket error observed:", event);
      },
    });

    // Activate the client (establishes the WebSocket connection)
    client.activate();

    // Save client to state
    setStompClient(client);

    // Cleanup on unmount
    return () => {
      client.deactivate();
    };
  }, [token]);


  // Use isConnected before sending
  useEffect(() => {
    if (message.newMessage && stompClient && isConnected) {
      stompClient.publish({
        destination: "/app/message",
        body: JSON.stringify(message.newMessage),
      });
  
      // Update messages immediately after sending
      setMessages((prev) => [...prev, message.newMessage]);
    }
  }, [message.newMessage, stompClient, isConnected]);



  // Callback to handle received messages from WebSocket
  const onMessageReceive = (payload) => {
    const receivedMessage = JSON.parse(payload.body);
    setMessages((prevMessages) => [...prevMessages, receivedMessage]);
  };


  // Effect to subscribe to a chat when connected
  useEffect(() => {
    if (!stompClient || !isConnected || !currentChat?.id) return;
  
    // Determine the correct subscription path
    const destination = currentChat.isGroupChat
      ? `/group/${currentChat.id}`
      : `/user/${currentChat.id}`;
  
    console.log("Subscribing to:", destination);
  
    // Unsubscribe from any previous subscription before making a new one
    const subscription = stompClient.subscribe(destination, (payload) => {
      const receivedMessage = JSON.parse(payload.body);
      console.log("Received message:", receivedMessage);
      setMessages((prev) => [...prev, receivedMessage]);
    });
  
    // Cleanup: Unsubscribe when chat changes or component unmounts
    return () => {
      subscription.unsubscribe();
    };
  }, [stompClient, isConnected, currentChat]);

  

  // Effect to set the messages state from the store
  useEffect(() => {
    if (message.messages) {
      setMessages(message.messages);
    }
  }, [message.messages]);

  // Effect to get all messages when the current chat changes
  useEffect(() => {
    if (currentChat?.id && token) {
      dispatch(getAllMessages({ chatId: currentChat.id, token }));
    }
  }, [currentChat, token, dispatch]);

  useEffect(() => {
    if (chat.createdChat) {
      setCurrentChat(chat.createdChat);
    }
  }, [chat.createdChat]);

  // Effect to get user chats and groups
  useEffect(() => {
    dispatch(getUsersChat({ token }));
  }, [chat.createdChat, chat.createdGroup]);

  // Function to handle opening the user menu
  const handleClick = (e) => {
    setAnchorEl(e.currentTarget);
  };

  // Function to handle closing the user menu
  const handleClose = () => {
    setAnchorEl(null);
  };

  // Function to handle clicking on a chat card
  const handleClickOnChatCard = (userId) => {
    dispatch(createChat({ token, data: { userId } }));
  };

  // Function to handle user search
  const handleSearch = (keyword) => {
    dispatch(searchUser({ keyword, token }));
  };

  // Function to create a new message
  const handleCreateNewMessage = () => {
    dispatch(
      createMessage({
        token,
        data: { chatId: currentChat.id, content: content },
      })
    );
    setContent(""); // Clear content after sending
  };

  // Effect to get the current user's information
  useEffect(() => {
    if (token) {
      dispatch(currentUser(token));
      dispatch(getUsersChat({ token }));
    }
  }, [token, dispatch]);

  // Function to set the current chat
  const handleCurrentChat = (item) => {
    setCurrentChat(item);
    setMessages([]); // Clear messages when switching chats
    dispatch(getAllMessages({ chatId: item.id, token })); // Fetch messages for the selected chat
  };

  // Effect to fetch messages when chat changes
  useEffect(() => {
    if (chat?.chats) {
      if (Array.isArray(chat.chats)) {
        chat.chats.forEach((item) => {
          dispatch(getAllMessages({ chatId: item.id, token }));
        });
      } else {
        // If it's an object, iterate over its values
        Object.values(chat.chats).forEach((item) => {
          dispatch(getAllMessages({ chatId: item.id, token }));
        });
      }
    }
  }, [chat?.chats, token, dispatch]);

  // Effect to update lastMessages when messages change
  useEffect(() => {
    const prevLastMessages = { ...lastMessages };
    if (message.messages && message.messages.length > 0) {
      message.messages.forEach((msg) => {
        prevLastMessages[msg.chat.id] = msg;
      });

      setLastMessages(prevLastMessages);
    }
  }, [message.messages]);

  // Function to navigate to the user's profile
  const handleNavigate = () => {
    setIsProfile(true);
  };

  // Function to close the user's profile
  const handleCloseOpenProfile = () => {
    setIsProfile(false);
  };

  // Function to handle creating a new group
  const handleCreateGroup = () => {
    setIsGroup(true);
  };

  // Function to handle user logout
  const handleLogout = () => {
    dispatch(logoutAction());
    router.push("/signin");
  };

  // Effect to check if the user is authenticated
  useEffect(() => {
    if (!auth.reqUser) {
      router.push("/signin");
    }
  }, [auth.reqUser]);
  return (

    <div className="relative">
    <div className="w-[100vw] py-14 bg-[#00a884]">
      <div className="flex bg-[#f0f2f5] h-[90vh] absolute top-[5vh] left-[2vw] w-[96vw]">
        <div className="left w-[30%] h-full bg-[#e8e9ec]">
          {isProfile && (
            <div className="w-full h-full">
              <Profile handleCloseOpenProfile={handleCloseOpenProfile} />
            </div>
          )}
          {isGroup && <CreateGroup setIsGroup={setIsGroup} />}
          {!isProfile && !isGroup && (
            <div className="w-full">
              <ProfileSection
                auth={auth}
                isProfile={isProfile}
                isGroup={isGroup}
                handleNavigate={handleNavigate}
                handleClick={handleClick}
                handleCreateGroup={handleCreateGroup}
                handleLogout={handleLogout}
                handleClose={handleClose}
                open={open}
                anchorEl={anchorEl}
              />
              <SearchBar
                querys={querys}
                setQuerys={setQuerys}
                handleSearch={handleSearch}
              />
              <ChatList
                querys={querys}
                auth={auth}
                chat={chat}
                lastMessages={lastMessages}
                handleClickOnChatCard={handleClickOnChatCard}
                handleCurrentChat={handleCurrentChat}
              />
            </div>
          )}
        </div>
         {/* Default WhatsApp Page */}
         {!currentChat?.id && (
            <div className="w-[70%] flex flex-col items-center justify-center h-full">
              <div className="max-w-[70%] text-center">
                <img
                  className="ml-11 lg:w-[75%] "
                  src="https://cdn.pixabay.com/photo/2015/08/03/13/58/whatsapp-873316_640.png"
                  alt="whatsapp-icon"
                />
                <h1 className="text-4xl text-gray-600">WhatsApp Web</h1>
                <p className="my-9">
                  Send and receive messages with WhatsApp and save time.
                </p>
              </div>
            </div>
          )}

          {/* Message Section */}
          {currentChat?.id && (
            <div className="w-[70%] relative  bg-blue-200">
              <div className="header absolute top-0 w-full bg-[#f0f2f5]">
                <div className="flex justify-between">
                  <div className="py-3 space-x-4 flex items-center px-3">
                    <img
                      className="w-10 h-10 rounded-full"
                      src={
                        currentChat.group
                          ? currentChat.chat_image ||
                            "https://media.istockphoto.com/id/521977679/photo/silhouette-of-adult-woman.webp?b=1&s=170667a&w=0&k=20&c=wpJ0QJYXdbLx24H5LK08xSgiQ3zNkCAD2W3F74qlUL0="
                          : auth.reqUser?.id !== currentChat.users[0]?.id
                          ? currentChat.users[0]?.profile ||
                            "https://media.istockphoto.com/id/521977679/photo/silhouette-of-adult-woman.webp?b=1&s=170667a&w=0&k=20&c=wpJ0QJYXdbLx24H5LK08xSgiQ3zNkCAD2W3F74qlUL0="
                          : currentChat.users[1]?.profile ||
                            "https://media.istockphoto.com/id/521977679/photo/silhouette-of-adult-woman.webp?b=1&s=170667a&w=0&k=20&c=wpJ0QJYXdbLx24H5LK08xSgiQ3zNkCAD2W3F74qlUL0="
                      }
                      alt="profile"
                    />
                    <p>
                      {currentChat.group
                        ? currentChat.chatName
                        : auth.reqUser?.id !== currentChat.users[0]?.id
                        ? currentChat.users[0].name
                        : currentChat.users[1].name}
                    </p>
                  </div>
                  <div className="flex py-3 space-x-4 items-center px-3">
                    <AiOutlineSearch />
                    <BsThreeDotsVertical />
                  </div>
                </div>
              </div>

              {/* Message Section */}
              <div className="px-10 h-[85vh] overflow-y-scroll pb-10" ref={messageContainerRef}>
                <div className="space-y-1 w-full flex flex-col justify-center items-end  mt-20 py-2">
                  {messages?.length > 0 &&
                    messages?.map((item, i) => (
                      <MessageCard
                        key={i}
                        isReqUserMessage={item?.user?.id !== auth?.reqUser?.id}
                        content={item.content}
                        timestamp={item.timestamp}
                        profilePic={item?.user?.profile || "https://media.istockphoto.com/id/521977679/photo/silhouette-of-adult-woman.webp?b=1&s=170667a&w=0&k=20&c=wpJ0QJYXdbLx24H5LK08xSgiQ3zNkCAD2W3F74qlUL0="}
                      />
                    ))}
                </div>
              </div>

              {/* Footer Section */}
              <div className="footer bg-[#f0f2f5] absolute bottom-0 w-full py-3 text-2xl">
                <div className="flex justify-between items-center px-5 relative">
                  <BsEmojiSmile className="cursor-pointer" />
                  <ImAttachment />

                  <input
                    className="py-2 outline-none border-none bg-white pl-4 rounded-md w-[85%]"
                    type="text"
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="Type message"
                    value={content}
                    onKeyPress={(e) => {
                      if (e.key === "Enter") {
                        handleCreateNewMessage();
                        setContent("");
                      }
                    }}
                  />
                  <BsMicFill />
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
        
export default HomePage;