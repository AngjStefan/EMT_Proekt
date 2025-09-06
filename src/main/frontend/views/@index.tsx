import { Button } from '@vaadin/react-components/Button';
import {useEffect, useState} from "react";
import {AssistantService, ProductWebService} from "Frontend/generated/endpoints";
import {GridColumn} from "@vaadin/react-components/GridColumn";
import {Grid} from "@vaadin/react-components/Grid";
import {MessageInput} from "@vaadin/react-components/MessageInput";
import {nanoid} from "nanoid";
import {SplitLayout} from "@vaadin/react-components/SplitLayout";
import Message, {MessageItem} from "../components/Message";
import MessageList from "Frontend/components/MessageList";
import Product from "Frontend/generated/backend/data/Product";

export default function Index() {
  const [chatId, setChatId] = useState(nanoid());
  const [working, setWorking] = useState(false);
  const [showDatabase, setShowDatabase] = useState(false);
  const [products, setProducts] = useState<Product[]>([]);
  const [messages, setMessages] = useState<MessageItem[]>([{
    role: 'assistant',
    content: 'Welcome! I\'m your virtual assistant. How can I help you?'
  }]);

  useEffect(() => {
    // Update products when we have received the full response
    if (!working) {
      ProductWebService.getProducts().then(setProducts);
    }
  }, [working]);

  function addMessage(message: MessageItem) {
    setMessages(messages => [...messages, message]);
  }

  function appendToLatestMessage(chunk: string) {
    setMessages(messages => {
      const latestMessage = messages[messages.length - 1];
      latestMessage.content += chunk;
      return [...messages.slice(0, -1), latestMessage];
    });
  }

  async function sendMessage(message: string) {
    setWorking(true);
    addMessage({
      role: 'user',
      content: message
    });
    let first = true;
    AssistantService.chat(chatId, message)
        .onNext(token => {
          if (first && token) {
            addMessage({
              role: 'assistant',
              content: token
            });

            first = false;
          } else {
            appendToLatestMessage(token);
          }
        })
        .onError(() => setWorking(false))
        .onComplete(() => setWorking(false));
  }

//  style={{ marginLeft: "15%", marginRight: "15%" }}
    // marginBottom: '3.6rem'
  return (
      <div
          style={{
            display: 'flex',
            height: '100%',           // full viewport height
            justifyContent: 'center',  // horizontal centering
          }}
      >
        <div
            style={{
              display: 'flex',
              flexDirection: 'column',
              gap: '1rem',
              padding: '1rem',
              boxSizing: 'border-box',
              width: '40%',
              marginTop: '1rem'
            }}
        >
            <div style={{ display: 'flex', justifyContent: 'center' }}>
                
            </div>
            
            <div style={{ display: 'flex', justifyContent: 'center' }}>
                <img
                    src="images/vr-asistant.png"
                    alt="GPT-4 Turbo"
                    style={{ width: '80%', height: 'auto', marginBottom: '3.6rem' }}
                />
            </div>
          

          <Button theme="tertiary" onClick={() => setShowDatabase(!showDatabase)}>
            {showDatabase ? 'Hide Database' : 'Show Database'}
          </Button>

          <MessageList messages={messages} className="flex-grow overflow-scroll"/>
          <MessageInput onSubmit={e => sendMessage(e.detail.value)}/>
        </div>

        {/* Right panel */}
        {showDatabase && (
            <div
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '1rem',
                  padding: '1rem',
                  boxSizing: 'border-box',
                  width: '45%',
                  marginTop: '1rem'
                }}
            >
              <h2 style={{color: 'hsla(213, 100%, 43%, 1)', textAlign: 'center'}}>База на податоци</h2>
              <Grid items={products}>
                <GridColumn
                    path="name"
                    autoWidth
                    header={
                      <span style={{ color: 'hsla(213, 100%, 43%, 1)', fontWeight: 'bold' }}>Име</span>
                }
                />
                <GridColumn
                    path="priceInMkd"
                    autoWidth
                    header={
                      <span style={{ color: 'hsla(213, 100%, 43%, 1)', fontWeight: 'bold' }}>Цена (ден.)</span>
                    }
                />
                <GridColumn
                    path="market"
                    autoWidth
                    header={
                      <span style={{ color: 'hsla(213, 100%, 43%, 1)', fontWeight: 'bold' }}>Маркет</span>
                    }
                />
              </Grid>
            </div>
        )}
      </div>
  );
}
