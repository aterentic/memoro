var NoteItem  = React.createClass({
    render: function() {
	return (
	    <li className="note-item">
		<input type="checkbox" checked={this.props.checked==="true"} onChange={function() { return false; }} />
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
	var items = this.state.data.items.map(function(item) {
	    return (
		    <NoteItem text={item.text} checked={item["checked?"]} key={item.id}/>
	    );
	});
	return (
	    <ul className="note">
		{items}
	    </ul>
	);
    }
});

React.render(
    <Note/>,
    document.getElementById('content')
);

