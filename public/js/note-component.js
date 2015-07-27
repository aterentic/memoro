var NoteItem  = React.createClass({
    render: function() {
	function nop() {
	    return false;
	}
	
	function render_checkbox(props) {
	    return (
		<input type="checkbox" checked={props.checked==="true"} onChange={nop} />
	    );
	}
	
	return (
	    <li className="note-item">
		{render_checkbox(this.props)}
	        {this.props.text}
	    </li>
	);
    }
});

var Note = React.createClass({
    getInitialState: function() {
	return {data: {items: []}};
    },
    componentDidMount: function () {
	var url = "notes/" + location.queryParams["note"] + "/items";
	$.get(url).done(function(data) {
	    this.setState({data: {items: data}});
	}.bind(this));
    },
    render: function() {
	function render_item(item) {
	    return (
		    <NoteItem text={item["text"]} checked={item["checked?"]} key={item["id"]}/>
	    );
	}
	return (
	    <ul className="note">
		{this.state.data.items.map(render_item)}
	    </ul>
	);
    }
});

React.render(
    <Note/>,
    document.getElementById('content')
);

