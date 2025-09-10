import { Button } from '@vaadin/react-components/Button';
import { useEffect, useState } from "react";
import { AssistantService, ProductWebService } from "Frontend/generated/endpoints";
import { GridColumn } from "@vaadin/react-components/GridColumn";
import { Grid } from "@vaadin/react-components/Grid";
import { MessageInput } from "@vaadin/react-components/MessageInput";
import { nanoid } from "nanoid";
import { MessageItem } from "../components/Message";
import MessageList from "Frontend/components/MessageList";
import Product from "Frontend/generated/backend/data/Product";
import { TextField } from "@vaadin/react-components/TextField";

// plain JS structure for paged products
type PagedProducts = {
    content: Product[];
    totalElements: number;
};

export default function Index() {
    const [chatId] = useState(nanoid());
    const [working, setWorking] = useState(false);
    const [showDatabase, setShowDatabase] = useState(false);
    const [pagedProducts, setPagedProducts] = useState<PagedProducts>({
        content: [],
        totalElements: 0,
    });
    const [searchText, setSearchText] = useState("");
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 20;
    const [messages, setMessages] = useState<MessageItem[]>([{
        role: 'assistant',
        content: 'Здраво! Како можам да ти помогнам?'
    }]);

    // Fetch products whenever currentPage or searchText changes
    useEffect(() => {
        const fetchProducts = async () => {
            const result = searchText && searchText.trim() !== ""
                ? await ProductWebService.findAllProductsByName(searchText, currentPage, pageSize)
                : await ProductWebService.getProducts(currentPage, pageSize);

            setPagedProducts({
                content: result.content as Product[],
                totalElements: result.totalElements,
            });
        };
        fetchProducts();
    }, [currentPage, searchText]);

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
        addMessage({ role: 'user', content: message });
        let first = true;
        AssistantService.chat(chatId, message)
            .onNext(token => {
                if (first && token) {
                    addMessage({ role: 'assistant', content: token });
                    first = false;
                } else {
                    appendToLatestMessage(token);
                }
            })
            .onError(() => setWorking(false))
            .onComplete(() => setWorking(false));
    }

    const handleSearch = (value: string) => {
        setSearchText(value);
        setCurrentPage(0); // reset to first page on search
    };

    const totalPages = Math.ceil(pagedProducts.totalElements / pageSize);

    return (
        <div style={{ display: 'flex', height: '100%', justifyContent: 'center' }}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', padding: '1rem', boxSizing: 'border-box', width: '40%', marginTop: '1rem' }}>
                <div style={{ display: 'flex', justifyContent: 'center' }}></div>
                <div style={{ display: 'flex', justifyContent: 'center' }}>
                    <img src="images/vr-asistant.png" alt="GPT-4 Turbo" style={{ width: '80%', height: 'auto', marginBottom: '3.6rem' }}/>
                </div>

                <Button theme="tertiary" onClick={() => setShowDatabase(!showDatabase)}>
                    {showDatabase ? 'Hide Database' : 'Show Database'}
                </Button>

                <MessageList messages={messages} className="flex-grow overflow-scroll"/>
                <MessageInput onSubmit={e => sendMessage(e.detail.value)}/>
            </div>

            {/* Right panel */}
            {showDatabase && (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', padding: '1rem', boxSizing: 'border-box', width: '45%', marginTop: '1rem' }}>
                    <h2 style={{color: 'hsla(213, 100%, 43%, 1)', textAlign: 'center'}}>База на податоци</h2>

                    <TextField
                        placeholder="Пребарај по име..."
                        clearButtonVisible
                        value={searchText}
                        onValueChanged={(e) => handleSearch(e.detail.value)}
                    />

                    <Grid items={pagedProducts.content}>
                        <GridColumn path="name" autoWidth header={<span style={{ color: 'hsla(213, 100%, 43%, 1)', fontWeight: 'bold' }}>Име</span>} />
                        <GridColumn path="priceInMkd" autoWidth header={<span style={{ color: 'hsla(213, 100%, 43%, 1)', fontWeight: 'bold' }}>Цена (ден.)</span>} />
                        <GridColumn path="market" autoWidth header={<span style={{ color: 'hsla(213, 100%, 43%, 1)', fontWeight: 'bold' }}>Маркет</span>} />
                    </Grid>

                    {/* Pagination controls */}
                    <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem', marginTop: '0.5rem' }}>
                        <Button disabled={currentPage === 0} onClick={() => setCurrentPage(p => Math.max(p - 1, 0))}>Prev</Button>
                        <span>Page {currentPage + 1} of {totalPages}</span>
                        <Button disabled={currentPage + 1 >= totalPages} onClick={() => setCurrentPage(p => Math.min(p + 1, totalPages - 1))}>Next</Button>
                    </div>
                </div>
            )}
        </div>
    );
}
